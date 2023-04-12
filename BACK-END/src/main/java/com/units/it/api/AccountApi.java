package com.units.it.api;

import com.units.it.entities.AccountProxy;
import com.units.it.filters.RoleFilter;
import com.units.it.helpers.AccountHelper;
import com.units.it.helpers.FileHelper;
import com.units.it.security.Jwt;
import com.units.it.entities.Account;
import com.units.it.entities.File;
import com.units.it.utils.Consts;
import com.units.it.utils.DevException;
import com.units.it.utils.Misc;
import com.units.it.utils.PasswordSaltHashCheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Path("/accounts")
public class AccountApi {

    @Context
    public HttpServletRequest request;
    @Context
    public HttpServletResponse response;

    /**
     * Registration web Service
     *
     * @param registrationAccount account we want to register
     * @return registration done message or BAD_REQUEST
     */
    @POST
    @Path("/registration")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registration(Account registrationAccount) {
        try {
            if (Consts.debug) {
                Misc.printData(registrationAccount, "registration");
            }
            /*Admin: can register admin or uploader
            uploader: can register only consumer
             */
            if (!registrationAccount.getRole().equals(Consts.CONSUMER)) {
                if (!RoleFilter.roleFilter(request, Consts.ADMINISTRATOR, true)) {
                    throw new DevException("Registration not allowed only an administrator can do");
                }
            }
            //verify the presence or not of the account saved in the db by using username
            if (AccountHelper.getById(Account.class, registrationAccount.getUsername()) != null) {
                throw new DevException("Username already exists");
            }
            //verify the syntax of the username we are registering based on the role
            if (Misc.isSyntaxUsernameWrong(registrationAccount.getUsername(), registrationAccount.getRole())) {
                throw new DevException("Invalid Username");
            }
            //if the account we want to register is NOT an UPLOADER we leave the logo field empty
            if (!registrationAccount.getRole().equals(Consts.UPLOADER)) {
                registrationAccount.setLogo("");
            }

            AccountHelper.saveDelayed(registrationAccount, true);

            if (Consts.debug) {
                System.out.println("REGISTRATION DONE --> " + registrationAccount.getUsername());
            }
            //Response with a message of correct registration
            return Response
                    .status(Response.Status.OK)
                    .entity("Registration Done - " + registrationAccount.getUsername())
                    .build();

        } catch (DevException e) {
            if (Consts.debug) {
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }

    /**
     * Modify web service: edit usernames is not allowed
     *
     * @param modifyAccount account with the changes
     * @return response with the new account modified or BAD_REQUEST
     */
    @POST
    @Path("/modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editInfo(Account modifyAccount) {
        try {
            if (Consts.debug) {
                Misc.printData(modifyAccount, "modify account");
            }
            //Take the token from the request and the role from the account we want to modify
            String token = Jwt.getTokenJWTFromRequest(request);
            String role = modifyAccount.getRole();
            String username;
            /*if the username is not set we intend to modify the information of the user which is logged in:
            the username is retrieved from the token.
            Otherwise we take the username from our modifyAccount and check the permission to modify it
             */
            if (modifyAccount.getUsername().equals("")) {
                username = Jwt.getUsernameFromJWT(token);
            } else {
                username = modifyAccount.getUsername();
                Misc.checkModifyPermissions(token, role);
            }

            Account dbAccount = AccountHelper.getById(Account.class, username);
            if (dbAccount == null || !role.equals(dbAccount.getRole())) {
                throw new DevException("Unexisting username to modify, you are modifying an: " + role);
            }

            //setting 2 flags
            AtomicBoolean modified = new AtomicBoolean(false);
            AtomicBoolean modifiedPassword = new AtomicBoolean(false);

            //if name is not empty and the name saved and the new one are not equal we set the new name
            if (!modifyAccount.getName().equals("") && !dbAccount.getName().equals(modifyAccount.getName())) {
                dbAccount.setName(modifyAccount.getName());
                modified.set(true);
            }
            //if email is not empty and the one saved and the new one are not equal we set the new mail
            if (!modifyAccount.getEmail().equals("") && !dbAccount.getEmail().equals(modifyAccount.getEmail())) {
                dbAccount.setEmail(modifyAccount.getEmail());
                modified.set(true);
            }
            //if the role of the account we are modifying is uploader the logo is not empty and it's different from the one already saved we set the new logo
            if (role.equals(Consts.UPLOADER) && !modifyAccount.getLogo().equals("") && !dbAccount.getLogo().equals(modifyAccount.getLogo())) {
                dbAccount.setLogo(modifyAccount.getLogo());
                modified.set(true);
            }
            //if the new password is not empty and it is not equal to the one already saved we set the new one
            if (!modifyAccount.getPassword().equals("") && !PasswordSaltHashCheck.isPasswordRight(modifyAccount.getPassword(), dbAccount.getPassword(), dbAccount.getSalt())) {
                dbAccount.setPassword(modifyAccount.getPassword());
                modified.set(true);
                modifiedPassword.set(true);
            }

            if (!modified.get()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("No data to be modify")
                        .build();
            }
            AccountHelper.saveNow(dbAccount, modifiedPassword.get());
            return Response
                    .status(Response.Status.OK)
                    .entity(new AccountProxy(dbAccount))
                    .build();

        } catch (Exception e) {
            if (Consts.debug) {
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }

    /**
     * Delete web service
     *
     * @param username username of the account we want to delete
     * @return response with message correct delete or  BAD_REQUEST
     */
    @DELETE
    @Path("/delete/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@PathParam("username") String username) {
        try {
            //retrieve the account we want to delete from the db by using the username
            Account dbDeleteAccount = AccountHelper.getById(Account.class, username);

            if (dbDeleteAccount == null) {
                throw new DevException("unexisting username");
            }

            /*retrieve the token and check if the username of the user logged in
            is equal to the one we want to delete. If it is we avoid the operation
             */
            String token = Jwt.getTokenJWTFromRequest(request);

            if (Jwt.getUsernameFromJWT(token).equals(dbDeleteAccount.getUsername())) {
                throw new DevException("Operation not allowed");
            }

            AccountHelper.deleteEntity(dbDeleteAccount);

            /* check the role of the account and delete teh documents associated with the deleted account.
            delete entity for UPLOADER
            set to null the file filed for CONSUMER
             */
            switch (dbDeleteAccount.getRole()) {
                case Consts.UPLOADER:
                    List<File> filesToDeleteUp = FileHelper.uploaderCompleteFileList(dbDeleteAccount.getUsername());
                    //filesToDeleteUp.forEach(FilesHelper::deleteEntity);
                    for (File f : filesToDeleteUp) {
                        FileHelper.deleteEntity(f);
                    }
                    break;
                case Consts.CONSUMER:
                    List<File> filesToDeleteCo = FileHelper.consumerFileList(dbDeleteAccount.getUsername());
                    for (File f : filesToDeleteCo) {
                        f.setFile(null);
                        FileHelper.saveNow(f);
                    }
                    break;
                default:
                    break;
            }

            return Response
                    .status(Response.Status.OK)
                    .entity("account " + username + " deleted")
                    .build();

        } catch (Exception e) {
            if (Consts.debug) {
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }

}
