package com.units.it.api;

import com.units.it.helpers.AccountHelper;
import com.units.it.helpers.FileHelper;
import com.units.it.security.Jwt;
import com.units.it.entities.Account;
import com.units.it.security.Token;
import com.units.it.entities.File;
import com.units.it.entities.FileProxy;
import com.units.it.utils.Consts;
import com.units.it.utils.DevException;
import com.units.it.utils.Misc;
import com.units.it.utils.ReportUploader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.util.Objects;

@Path("/files")
public class FilesApi {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    private String d;

    /**
     * Download web Service based on the id of the document
     *
     * @param id id of the document to download
     * @return response containing the file in octet_stream or BAD_REQUEST containing an error message
     */
    @GET
    @Path("/download/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response downloadFileFromSite(@PathParam("id") String id) {
        try {
            //retrieving the username of the consumer who wants to download the file
            String token = Jwt.getTokenJWTFromRequest(request);
            String usernameConsumer = Jwt.getUsernameFromJWT(token);

            //check if the username retrieved from the token is equal to the usernameConsumer field in file
            if (!usernameConsumer.equals(Objects.requireNonNull(FileHelper.getById(File.class, id)).getUsernameCons())) {
                throw new DevException("Destination consumer of the file is not the one logged in");
            }
            return download(id);

        } catch (Exception e) {
            if (Consts.debug) System.out.println(e.getMessage() + "\n");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }

    /**
     * Download direct web service
     *
     * @param fileId id of the file we want to download
     * @param token used to allow teh download operation based on the file id
     * @return response containing the file in octet_stream or BAD_REQUEST containing an error message
     */
    @GET
    @Path("/direct/download/{fileId}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response downloadFromLink(@PathParam("fileId") String fileId, @PathParam("token") String token) {
        if (!Token.verifyToken(token, fileId)){
            return Response.status(Response.Status.BAD_REQUEST).entity("ERR - Token Error").build();
        }

        return download(fileId);
    }

    /**
     * Download procedure of the file in which we set the data view and the ip address and
     * give back the file
     *
     * @param id id of the document to download
     * @return response containing the file in octet_stream or NOT_FOUND containing an error message
     */
    private Response download(String id) {
        File fileToDownload = FileHelper.getById(File.class, id);
        //check both if the entity is null or the field file is null
        if (fileToDownload == null || fileToDownload.getFile() == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("ERR - File Not Found ")
                    .build();
        }

        /*if the file is not already viewed we set the data view and the Ip
        address from which the request comes
         */
        if (fileToDownload.getDataView().equals("")) {
            String d = Misc.getDataString();
            fileToDownload.setDataView(d);
            fileToDownload.setIP(request.getRemoteAddr());
            FileHelper.saveNow(fileToDownload);
        }

        // https://www.java2novice.com/restful-web-services/jax-rs-download-file/
        //https://stackoverflow.com/questions/12239868/whats-the-correct-way-to-send-a-file-from-rest-web-service-to-client
        return Response
                .ok(fileToDownload.getFile(), MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .header("Content-Disposition", "attachment; filename=\"" + fileToDownload.getName() + "\"")
                .build();
    }

    /**
     * Upload web service
     *
     * @param reportUploader indicating the support entity for the uploader report
     * @return return the list of the files of the uploader or BAD_REQUEST
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadFile(ReportUploader reportUploader) {
        try {
            //get the token and from it the username who made the request (this request is made by an uploader)
            String token = Jwt.getTokenJWTFromRequest(request);
            String usernameUpl = Jwt.getUsernameFromJWT(token);

            //setting the fields  with the uploader name and the data upload
            reportUploader.setUsernameUpl(usernameUpl);
            reportUploader.setDataUp(Misc.getDataString());

            Account dbAccount = AccountHelper.getById(Account.class, reportUploader.getUsernameConsumer());

            //If the consumer account is not present in the db we add a new one sending an email with a temporary password
            if (dbAccount == null) {
                if (Misc.isSyntaxUsernameWrong(reportUploader.getUsernameConsumer(), Consts.CONSUMER)) {
                    throw new DevException("Invalid Consumer username.");
                }

                if (reportUploader.getEmailConsumer().equals("") || reportUploader.getNameConsumer().equals("")) {
                    throw new DevException("Missing Informations");
                }

                //https://www.techiedelight.com/generate-random-alphanumeric-password-java/
                //setting the password and save the new account in the db
                final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

                SecureRandom random = new SecureRandom();
                StringBuilder sb = new StringBuilder();

                // each iteration of the loop randomly chooses a character from the given
                // ASCII range and appends it to the `StringBuilder` instance

                for (int i = 0; i < 12; i++)
                {
                    int randomIndex = random.nextInt(chars.length());
                    sb.append(chars.charAt(randomIndex));
                }

                String temporaryPassword = sb.toString();
                Account newConsumer = new Account(reportUploader.getUsernameConsumer(), temporaryPassword, reportUploader.getNameConsumer(), reportUploader.getEmailConsumer(), Consts.CONSUMER, "");
                AccountHelper.saveNow(newConsumer, true);

                //Sending an email to the new account registered
                String indirectRegistrationMail = Misc.sendMailNewAccount(newConsumer, temporaryPassword, usernameUpl);

                if (Consts.debug) {
                    System.out.println(indirectRegistrationMail);
                }

            } else {
                //Otherwise we set the name of the consumer and the e-mail in the report
                reportUploader.setNameConsumer(dbAccount.getName());
                reportUploader.setEmailConsumer(dbAccount.getEmail());
            }

            //we create a new file and save it in the db
            File newFile = new File(reportUploader.getUsernameUpl(), reportUploader.getUsernameConsumer(), reportUploader.getFile(), reportUploader.getFileName(), reportUploader.getDataUp(), reportUploader.getHashtag());
            FileHelper.saveDelayed(newFile);

            //send a notice to inform the consumer about the new file
            String notification = Misc.sendNotice(reportUploader, usernameUpl, newFile.getId(), newFile.getName(), newFile.getHashtag());

            if (Consts.debug) {
                System.out.println(notification);
            }

            return Response
                    .status(Response.Status.OK)
                    .entity(new FileProxy(newFile))
                    .build();

        } catch (Exception e) {
            if (Consts.debug){
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
     * @param fileId id of the file we want to delete
     * @return a response with a message to confirm the delete or File Not Found or a BAD_REQUEST
     */
    @Path("/delete/{fileId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteFile(@PathParam("fileId") String fileId) {
        try {
            //retrieve the file from the db by the id
            File dbFile = FileHelper.getById(File.class, fileId);
            if (dbFile == null || dbFile.getFile() == null)
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("ERR - File Not Found")
                        .build();

            //retrieve the token to obtain the username fo the uploader who made the request
            String token = Jwt.getTokenJWTFromRequest(request);
            String usernameUpl = Jwt.getUsernameFromJWT(token);

            if (!dbFile.getUsernameUpl().equals(usernameUpl)) {
                throw new DevException("This file doesn't belong to the uploader who send delete command");
            }

            //delete the file by setting the file field to null and save the new value
            dbFile.setFile(null);
            FileHelper.saveDelayed(dbFile);
            return Response
                    .status(Response.Status.OK)
                    .entity("delete file " + fileId + " completed")
                    .build();

        } catch (Exception e) {
            if (Consts.debug){
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }
}