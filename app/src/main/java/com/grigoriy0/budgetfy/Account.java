package com.grigoriy0.budgetfy;

public class Account {
    public enum Type {
        WALLET,
        CREDIT_CARD
    }

    private final Float startValue;

    private Float currentValue;

    private String name;

    private Type type;

    public Account(String name, float startValue, float currentValue, Type accountType) {
        this.startValue = startValue;
        this.name = name;
        this.currentValue = currentValue;
        this.type = accountType;
    }

    public Float getStartValue() {
        return startValue;
    }

    public Float getCurrentValue() {
        return currentValue;
    }

    public Type getType() {
            return type;
    }

    public String getName() {
        return name;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
