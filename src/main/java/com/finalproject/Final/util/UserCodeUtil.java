package com.finalproject.Final.util;

public class UserCodeUtil {

    public static String formatUserCode(String roleId, String id) {

        return switch (roleId) {

            case "Admin" -> String.format("AD%04d", id);

            case "Teacher" -> String.format("T%04d", id);

            case "Student" -> String.format("ST%04d", id);

            default -> String.valueOf(id);
        };
    }
}