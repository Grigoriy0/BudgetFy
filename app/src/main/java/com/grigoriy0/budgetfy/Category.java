package com.grigoriy0.budgetfy;

public enum Category {
    TRANSPORT,
    UNIVERSITY,
    FOOD,
    CAFE,
    PHONE,
    BARBER,
    RENT,
    OTHER,


    STIPEND,
    PARENTS,
    GIFT,
    SALARY;

    public boolean isLoss() {
        final Category category = this;
        return category != PARENTS &&
                category != STIPEND &&
                category != GIFT &&
                category != SALARY;
    }

    @Override
    public String toString() {
        final String category = super.toString();
        return category.substring(0, 1) + category.substring(1).toLowerCase();
    }
}