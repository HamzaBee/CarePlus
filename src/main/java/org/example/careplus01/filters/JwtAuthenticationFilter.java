package org.example.careplus01.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.careplus01.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        System.out.println("log in attempt");
        System.out.println("Method: " + request.getMethod());
        System.out.println("Content-Type: " + request.getContentType());

        String username = null;
        String password = null;

        // check if the request sent in a format of json
        if (request.getContentType() != null && request.getContentType().contains("application/json")) {
            try {
                // read json body
                LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
                username = loginRequest.getUsername();
                password = loginRequest.getPassword();
                System.out.println("JSON Login Request");
            } catch (IOException e) {
                throw new RuntimeException("Bad Json body request", e);
            }
        } else {
            // using x www form instead
            username = request.getParameter("username");
            password = request.getParameter("password");
            System.out.println("x www form Login Request");
        }

        System.out.println("Username: " + username);
        System.out.println("Password present: " + (password != null));

        if (username == null || password == null) {
            throw new RuntimeException("Username and password must not be null");
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        System.out.println("=== you have logged in successfully ===");

        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        var secretKey = Keys.hmacShaKeyFor(JwtUtil.SECRET.getBytes());

        String accessToken = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JwtUtil.EXPIRES_ACCESS_TOKEN))
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JwtUtil.EXPIRES_REFRESH_TOKEN))
                .signWith(secretKey)
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access-token", accessToken);
        tokens.put("refresh-token", refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        System.out.println("=== your attempt to log in failed ===");
        System.out.println("Error: " + failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> error = new HashMap<>();
        error.put("error", "Authentication failed");
        error.put("message", failed.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    // Inner class for JSON deserialization
    private static class LoginRequest {
        private String username;
        private String password;


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}