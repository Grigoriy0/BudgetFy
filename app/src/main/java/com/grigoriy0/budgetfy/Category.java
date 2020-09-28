package com.grigoriy0.budgetfy;

import androidx.room.TypeConverter;

public enum Category {
    TRANSPORT,
    UNIVERSITY,
    FOOD,
    CAFE,
    PHONE,
    BARBER,
    RENT,
    MISCELLANEOUS,


    SCHOLARSHIP,
    PARENTS,
    GIFT;

    public boolean isLoss() {
        final Category category = this;
        return category != PARENTS &&
                category != SCHOLARSHIP &&
                category != GIFT;
    }

    @Override
    public String toString() {
        final String category = super.toString();
        return category.substring(0, 1) + category.substring(1).toLowerCase();
    }
}