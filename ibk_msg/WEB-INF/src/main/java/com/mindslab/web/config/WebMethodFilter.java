package com.mindslab.web.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class WebMethodFilter extends OncePerRequestFilter { 

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                    throws ServletException, IOException { 
        if (request.getMethod().equals("OPTIONS") || request.getMethod().equals("TRACE") || request.getMethod().equals("PUT") || request.getMethod().equals("PATCH") || request.getMethod().equals("DELETE")){
            response.getWriter().println("method not allowed");
        } else { 
            filterChain.doFilter(request, response); 
        } 
    }
}