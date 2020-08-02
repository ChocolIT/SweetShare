package com.example.sweetshare.util;

public class inputDataValidator {
    public static class passValidator {
        boolean hasMinLength(String pass) {
            if (pass.length() < 6) {
                return false;
            }
            return true;
        }
    }
}
