package com.fazemeright.firebase_api__library.utils;

public class AppUtils {
    public static AppUtils appUtils;

    public static AppUtils getInstance() {
        if (appUtils == null) {
            appUtils = new AppUtils();
        }
        return appUtils;
    }


    public static String getRollNoFromEmail(String currentUserEmail) {
        if (currentUserEmail == null) {
            return null;
        } else {
            return currentUserEmail.substring(0, currentUserEmail.indexOf("@"));
        }
    }
}
