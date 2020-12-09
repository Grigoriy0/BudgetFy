package com.grigoriy0.budgetfy.accountdetails;

import android.graphics.Color;

import com.grigoriy0.budgetfy.R;

public enum Category {
    TRANSPORT("Transport"),
    EDUCATION("Education"),
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

    public boolean isLoss() {
        final Category category = this;
        return category != STIPEND && category != GIFT && category != SALARY;
    }

    public int getIconResId() {
        switch (type) {
            case "Transport": return R.drawable.ic_transport_90;
            case "Education": return R.drawable.ic_education_90;
            case "Food": return R.drawable.ic_food_90;
            case "Cafe": return R.drawable.ic_cafe_90;
            case "Phone": return R.drawable.ic_phone_90;
            case "Barber": return R.drawable.ic_barber_90;
            case "Rent": return R.drawable.ic_rent_90;
            case "Other": return R.drawable.ic_other_90;
            case "Gift": return R.drawable.ic_gift_90;
            case "Salary": case "Stipend": return R.drawable.ic_salary_90;
            default: return R.drawable.ic_food_90;
        }
    }

    public Integer getColor() {
        switch (type){
            case "Transport": return Color.LTGRAY;
            case "Education": return Color.BLUE;
            case "Food": return Color.YELLOW;
            case "Cafe": return Color.rgb(255, 165, 0); // orange
            case "Phone": return Color.MAGENTA;
            case "Barber": return Color.rgb(255, 192, 203); //pink
            case "Rent": return Color.GREEN;
            case "Other": return Color.GRAY;
            case "Gift": return Color.rgb(255, 165, 0); // orange
            case "Salary": return Color.RED;
            case "Stipend": return Color.rgb(0, 100, 0); // dark green
            default: return Color.rgb(255, 255, 255);
        }
    }

    @Override
    public String toString() {
        return type;
    }

    public static Category fromString(String string) {
        for (Category cat : Category.values())
            if (cat.type.equals(string)) return cat;

        return Category.OTHER;
    }
}
