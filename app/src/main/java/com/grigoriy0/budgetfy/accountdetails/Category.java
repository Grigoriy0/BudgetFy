package com.grigoriy0.budgetfy.accountdetails;

import android.graphics.Color;

import com.grigoriy0.budgetfy.R;

public enum Category {
    TRANSPORT("Transport"),
    UNIVERSITY("University"),
    FOOD("Food"),
    CAFE("Cafe"),
    PHONE("Phone"),
    BARBER("Barber"),
    RENT("Rent"),
    OTHER("Other"),

    STIPEND("Stipend"),
    GIFT("Gift"),
    SALARY("Salary");

    private final String type;

    Category(String type) {
        this.type = type;
    }

    private static final int[] colors = new int[]{
            Color.LTGRAY,
            Color.BLUE,
            Color.YELLOW,
            Color.rgb(255, 165, 0), // orange
            Color.DKGRAY,
            Color.rgb(255, 192, 203), //pink
            Color.GREEN,
            Color.GRAY,

            Color.rgb(255, 165, 0), // orange
            Color.RED,
            Color.rgb(0, 100, 0) // dark green
    };

    public static int[] getColors() {
        return colors;
    }

    public boolean isLoss() {
        final Category category = this;
        return category != STIPEND &&
                category != GIFT &&
                category != SALARY;
    }

    public int getIconResId() {
        switch (type) {
            case "Transport":
                return R.drawable.ic_transport_90;
            case "University":
                return R.drawable.ic_university_90;
            case "Food":
                return R.drawable.ic_food_90;
            case "Cafe":
                return R.drawable.ic_cafe_90;
            case "Phone":
                return R.drawable.ic_phone_90;
            case "Barber":
                return R.drawable.ic_barber_90;
            case "Rent":
                return R.drawable.ic_rent_90;
            case "Other":
                return R.drawable.ic_other_90;
            case "Gift":
                return R.drawable.ic_gift_90;
            case "Salary":
            case "Stipend":
                return R.drawable.ic_salary_90;
            default:
                return R.drawable.ic_food_90;
        }
    }

    @Override
    public String toString() {
        return type;
    }

    public static Category fromString(String string) {
        for (Category cat : Category.values()) {
            if (cat.type.equals(string))
                return cat;
        }
        return Category.OTHER;
    }
}