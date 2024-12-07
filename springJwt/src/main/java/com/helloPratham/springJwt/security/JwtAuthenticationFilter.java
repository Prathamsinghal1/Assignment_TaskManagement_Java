package com.helloPratham.springJwt.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Authorization: Bearer <JWT_TOKEN>
        String requestHeader = request.getHeader("Authorization");

        String userId = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);

            try {
                userId = this.jwtUtil.extractUserIdFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.error("Illegal Argument while fetching the username from the token", e);
            } catch (ExpiredJwtException e) {
                logger.error("JWT token has expired", e);
            } catch (MalformedJwtException e) {
                logger.error("Malformed JWT token", e);
            } catch (Exception e) {
                logger.error("An error occurred while processing the JWT token", e);
            }
        } else {
            logger.warn("Invalid Authorization header: Missing 'Bearer' token");
        }

        if (userId != null && jwtUtil.validateToken(token)) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            logger.info("JWT token validation failed for user: {}", userId);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized
        }

        filterChain.doFilter(request, response);
    }
}
