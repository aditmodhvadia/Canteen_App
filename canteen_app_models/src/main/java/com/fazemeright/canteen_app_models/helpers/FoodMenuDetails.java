package com.fazemeright.canteen_app_models.helpers;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FoodMenuDetails {
    // Declare the constants
    public static final String AVAILABLE = "Available";
    public static final String PRICE = "Price";
    public static final String RATING = "Rating";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({AVAILABLE, PRICE, RATING})
    // Create an interface for validating String types
    public @interface FilterColorDef {
    }
}
