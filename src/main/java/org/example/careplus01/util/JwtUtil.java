package org.example.careplus01.util;

public class JwtUtil {

    /*
     Secret key for JWT signing - MUST be at least 32 characters (256 bits) for HS256
      Current length: 58 characters (464 bits)
     */
    public static final String SECRET = "MySecureSecretKeyForJWTSigningCarePlusApplication2024!@#$";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final Long EXPIRES_ACCESS_TOKEN = 2 * 60 * 1000L;
    public static final Long EXPIRES_REFRESH_TOKEN = 15 * 60 * 1000L;
}
