package org.example.careplus01.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.careplus01.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //start

        String path = request.getServletPath();

        // Skip JWT validation for Thymeleaf pages and public endpoints
        if (path.equals("/login-page") ||
                path.equals("/admin-dashboard") ||
                path.equals("/dashboard") ||
                path.equals("/") ||
                path.equals("/refreshToken") ||
                path.startsWith("/css") ||
                path.startsWith("/js") ||
                path.startsWith("/images") ||
                path.startsWith("/static") ||
                path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }


        //end





        // skipping jwt validation for refreshing the token
        if (request.getServletPath().equals("/refreshToken")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtAuthorizationHeader = request.getHeader(JwtUtil.HEADER_STRING);

        if (jwtAuthorizationHeader != null && jwtAuthorizationHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
            try {

                String jwtToken = jwtAuthorizationHeader.substring(JwtUtil.TOKEN_PREFIX.length());


                var secretKey = Keys.hmacShaKeyFor(JwtUtil.SECRET.getBytes());


                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload();


                String username = claims.getSubject();


                List<String> roles = claims.get("roles", List.class);
                Collection<GrantedAuthority> authorities = new ArrayList<>();

                if (roles != null) {
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }


                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // Spring Security considers the user is authenticated
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                // Calls the controller
                filterChain.doFilter(request, response);

            } catch (Exception e) {

                System.err.println("JWT Authorization Error: " + e.getMessage());
                response.setHeader("Error-Message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {

            filterChain.doFilter(request, response);
        }

    }
    }




