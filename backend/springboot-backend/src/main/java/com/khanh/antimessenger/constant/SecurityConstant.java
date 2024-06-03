package com.khanh.antimessenger.constant;

public class SecurityConstant {
    public static final String ISSUER = "Thu Hanoi, LLC";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_HEADER = "Jwt-Token";
    public static final String AUDIENCE = "Thu Hanoi public user";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String TOKEN_EXPIRED_MESSAGE = "Your session has expired. Please login.";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String[] PUBLIC_URLS = {"/login", "/register", "/verify/**", "/account/reset-password/**", "/account/image/**", "/error"};
//    public static final String[] PUBLIC_URLS = {"/**"};
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1_800_000; //30 minutes in milisec
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 7_889_400_000L; //3 months in milisec
    public static final long VERIFICATION_URL_EXPIRATION_TIME = 84_400; //1 day in sec
    public static final String PUBLIC_KEY_FILE_CANNOT_BE_FOUND = "Public key file cannot be found";
    public static final String PRIVATE_KEY_FILE_CANNOT_BE_FOUND = "Private key file cannot be found";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%*.\\-_?]).{8,30}$"; //33,35-37,42,45,46,48-57,63-90,95,97-122
    public static final int BCRYPT_STRENGTH = 7;
}
