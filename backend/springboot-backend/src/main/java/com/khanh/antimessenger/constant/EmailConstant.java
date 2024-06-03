package com.khanh.antimessenger.constant;

public class EmailConstant {
//    public static final String SEND_EMAIL_SUCCESS = "Email was successfully sent";
//    public static final String SEND_EMAIL_FAIL = "An error occurred while sending email";
    public static final String ACCOUNT_VERIFY_MAIL_SUBJECT = "Account verification email";
    public static final String RESET_PASSWORD_BY_USER_MAIL_SUBJECT = "Reset password verification email";
    public static final String RESET_PASSWORD_BY_ADMIN_MAIL_SUBJECT = "Notification about your password reset";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_STARTTLS_ENABLED = "mail.smtp.starttls.enabled";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    public static final String SMTP_SSL_TRUST = "mail.smtp.ssl.trust";
    public static final String CC_EMAIL = "mail.smtp.host";
    public static final String FROM_EMAIL = "mail.smtp.host";
    public static final String MAIL_USERNAME = "antimessenger";
    public static final String MAIL_PASSWORD = "dyln ylub vbna ohit";
    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String DEFAULT_PORT = "587";

    public static final String RESET_PASSWORD_BY_USER_MAIL_TEMPLATE = "\n" +
            "<p>Hi <strong>%s<strong>,<p>\n" +
            "\n" +
            "<p>We have received a request to reset the password of an account linked to this email with username: %s!<p>\n" +
            "\n" +
            "If it was not you then please ignore this email and consider changing your credentials.\n" +
            "\n" +
            "If it was you then click below to reset your password:\n" +
            "\n" +
            "<a>%s<a>\n" +
            "\n" +
            "If you have problems, please paste the above URL into your web browser.";

    public static final String RESET_PASSWORD_BY_ADMIN_MAIL_TEMPLATE = "\n" +
            "<p>Hi <strong>%s<strong>,<p>\n" +
            "\n" +
            "<p>Your password has been reset by an admin<p>\n" +
            "\n" +
            "\n" +
            "Here is your new password:\n" +
            "\n" +
            "<a>%s<a>\n" +
            "\n" +
            "If it was not you then please contact to an admin to resolve the problem.";

    public static final String ACCOUNT_VERIFY_MAIL_TEMPLATE = "\n" +
            "<p>Hi <strong>%s<strong>,<p>\n" +
            "\n" +
            "<h3>Thanks for getting started with our messenger app Antimess!<h3>\n" +
            "\n" +
            "<p>We need a little more information to complete your registration, including a confirmation of your email address.<p>\n" +
            "\n" +
            "<p>Please click <a href='%s'>HERE<a> to confirm your email address<p>\n" +
            "\n" +
            "<p>If you have problems, please try again after 5 minutes.<p>";
}
