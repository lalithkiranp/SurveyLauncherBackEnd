package com.example.surveyapp.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.surveyapp.service.TokenBlacklistService;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
    	System.out.println("Authorization Header: " + request.getHeader("Authorization"));
    	System.out.println("🔥 JWT FILTER EXECUTING...");
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);
            
            if (blacklistService.isBlacklisted(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtUtil.validateToken(token)) {

                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);
                System.out.println("Extracted Role: " + role);
                // ✅ Assign ADMIN role
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singleton(() -> role)
                        );
                System.out.println("User authenticated: " + username);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
        
    }
}