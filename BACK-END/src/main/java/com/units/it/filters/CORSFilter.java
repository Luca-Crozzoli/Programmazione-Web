package com.units.it.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(asyncSupported = true)
public class CORSFilter implements Filter {

    public CORSFilter() {
    }

    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpRes = (HttpServletResponse) servletResponse;

        httpRes.addHeader("Access-Control-Allow-Origin", "*");
        httpRes.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE");
        httpRes.addHeader("Access-Control-Max-Age", "3600");
        httpRes.addHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, Accept-Charset");

        if (httpReq.getMethod().equals("OPTIONS")) {
            httpRes.setStatus(HttpServletResponse.SC_ACCEPTED);
        } else {
            chain.doFilter(httpReq, httpRes);
        }
    }

    public void destroy() {
    }


}

