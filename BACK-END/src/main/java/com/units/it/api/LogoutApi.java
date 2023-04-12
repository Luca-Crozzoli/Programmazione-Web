package com.units.it.api;

import com.units.it.entities.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/logout")
public class LogoutApi {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout() {
            /*HttpSession session = request.getSession();
            session.removeAttribute("token");
            session.invalidate();*/

        return Response
                .status(Response.Status.OK)
                .entity("Logout done")
                .build();
    }

}
