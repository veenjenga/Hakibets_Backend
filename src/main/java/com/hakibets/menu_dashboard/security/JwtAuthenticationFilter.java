package com.hakibets.menu_dashboard.security;

import com.hakibets.menu_dashboard.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //This tells Spring to create an instance of this class and manage it. It’s like registering the filter with the security team.
public class JwtAuthenticationFilter extends OncePerRequestFilter { //This names the class and makes it a filter by extending OncePerRequestFilter, which ensures it runs once for each incoming request.
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)// This is the main method that runs for every request. It overrides the parent class’s method to add our custom logic.
            throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter processing: " + request.getRequestURI() + " at " + new java.util.Date());
        /*This checks if the request URL starts with /api/auth/.
         If true, it skips the token check because these endpoints (login, register) don’t need tokens.
         */
        if (request.getRequestURI().startsWith("/api/auth/")) {
            System.out.println("Bypassing JWT authentication for /api/auth/**: " + request.getRequestURI());
            chain.doFilter(request, response);//passes the request to the next step in the process without checking the token.
            return;
        }

        // The block of code checks if a user is already logged in with a valid token
        String header = request.getHeader("Authorization");//Gets the Authorization header from the request, which should contain the token
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String username = jwtUtil.extractUsername(token);//Uses JwtUtil to get the username from the token.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {//Checks if a username was found and if no one is already authenticated. If both are true, it proceeds.
                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username, null, null);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);//Stores the token in Spring Security’s context, marking the user as logged in for this request.
                }
            }
        }
        chain.doFilter(request, response);//Passes the request to the next filter or the controller, whether authenticated or not.
    }
}