package com.grigoriy0.budgetfy.accountdetails;

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

    public boolean isLoss() {
        final Category category = this;
        return category != STIPEND &&
                category != GIFT &&
                category != SALARY;
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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}