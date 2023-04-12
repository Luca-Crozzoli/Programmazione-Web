package com.units.it.api;

import com.units.it.helpers.AccountHelper;
import com.units.it.security.Jwt;
import com.units.it.utils.PasswordSaltHashCheck;
import com.units.it.entities.Account;
import com.units.it.utils.Consts;
import com.units.it.utils.DevException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginApi {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    /**
     * Login web service
     *
     * @param accountLogin account who is trying to login
     * @return a response containing the token and the role of the account logged or BAD_REQUEST
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(Account accountLogin) {
        try {
            Account dbAccount = AccountHelper.getById(Account.class, accountLogin.getUsername());

            if (dbAccount == null) {
                throw new DevException("Unexisting Username");
            }

            if (!PasswordSaltHashCheck.isPasswordRight(accountLogin.getPassword(), dbAccount.getPassword(), dbAccount.getSalt())) {
                throw new DevException("Wrong Password");
            }
            if (Consts.debug) {
                System.out.println("Login done: " + accountLogin.getUsername() + "\n");
            }

            /*HttpSession session = request.getSession();
            session.setAttribute("token", token);
            session.setMaxInactiveInterval(30*60);*/

            return Response
                    .status(Response.Status.OK)
                    .entity(Jwt.generateJWT(accountLogin.getUsername(), dbAccount.getRole()))
                    .build();

        } catch (DevException e) {
            if (Consts.debug) {
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }
}
