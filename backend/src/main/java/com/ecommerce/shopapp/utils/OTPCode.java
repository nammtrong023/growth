package com.ecommerce.shopapp.utils;

import java.util.Random;

public class OTPCode {

    public static String generateOTP(int len) {
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int ranNo = new Random().nextInt(9);
            otpBuilder.append(ranNo);
        }

        return otpBuilder.toString();
    }
}
