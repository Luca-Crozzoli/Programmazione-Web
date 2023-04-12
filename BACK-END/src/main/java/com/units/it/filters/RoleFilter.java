package com.units.it.filters;

import com.units.it.utils.Consts;
import com.units.it.security.Jwt;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter
public class RoleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (roleFilter(request, Consts.ADMINISTRATOR, true)) {
            chain.doFilter(request, response);
        } else if (roleFilter(request, Consts.UPLOADER, true)) {
            chain.doFilter(request, response);
        } else if (roleFilter(request, Consts.CONSUMER, true)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    public static boolean roleFilter(ServletRequest request, String role, boolean checkJwtRole) {
        try {
            if (Consts.debug) {
                System.out.println("\n" + role.toUpperCase() + " FILTER for path " + ((HttpServletRequest) request).getPathInfo());
            }

            String token = Jwt.getTokenJWTFromRequest((HttpServletRequest) request);

            if (!Jwt.verifyJWT(token)) {
                throw new Exception("Invalid Token from role filter");
            }
            if (checkJwtRole) {
                String r = Jwt.getRoleFromJWT(token);
                if (!r.equals(role)) {
                    throw new Exception("It is not an " + role + "...");
                }
            }


            if (Consts.debug) {
                System.out.println("success (filter passed)");
            }
            return true;

        } catch (Exception e) {
            if (Consts.debug) {
                System.out.println("DENIED ACCESS (Role filter): " + e.getMessage());
                System.out.println("---------------------------------------------------");
            }
            return false;
        }
    }

    @Override
    public void destroy() {
    }

}