package com.example.addressBook.config;

import com.example.addressBook.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove "Bearer " part

            try {
                Long userId = jwtTokenService.verifyToken(token);
                if (userId != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Token expired\", \"status\": 401}");
                response.setContentType("application/json");
                return;
            } catch (JwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid token\", \"status\": 401}");
                response.setContentType("application/json");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
