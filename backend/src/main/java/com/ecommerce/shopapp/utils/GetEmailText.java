package com.ecommerce.shopapp.utils;

public class GetEmailText {
    public static String getEmailText(String url) {
        return  "<p style=\"font-family: 'Roboto', sans-serif; font-size: 16px; font-weight: normal; margin: 0; margin-bottom: 15px;\">\n" +
                "    Click the link below the reset your password.\n" +
                "</p>\n" +
                "<div class=\"btn-primary\" style=\"border-radius: 5px; text-align: center; background-color: #3498db;\">\n" +
                "    <a href=\"" + url + "\" style=\"border: solid 1px #3498db; border-radius: 5px; box-sizing: border-box; width: 100%; cursor: pointer; display: inline-block; font-size: 16px; font-weight: bold; margin: 0; padding: 12px 25px; text-decoration: none; text-transform: capitalize; background-color: #3498db; border-color: #3498db; color: #ffffff;\">\n" +
                "        Click to reset password\n" +
                "    </a>\n" +
                "</div>";
    }
}
