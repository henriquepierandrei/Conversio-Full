package com.tech.pierandrei.Conversio.core.utils;

import org.springframework.stereotype.Component;

@Component
public class EmailLoggerUtils {

    private static boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return email.contains("@") && !email.isBlank();
    }

    public static String maskEmail(String email) {
        if (email == null) {
            return "Email is null.";
        }

        if (isEmailValid(email)) {
            int atIndex = email.indexOf('@');
            return email.charAt(0) + "*****" + email.substring(atIndex);
        }
        return "Invalid emailDto format.";
    }



}
