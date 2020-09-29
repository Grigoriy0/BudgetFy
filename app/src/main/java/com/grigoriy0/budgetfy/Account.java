package com.grigoriy0.budgetfy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "account_table")
public class Account {
    public static abstract class Type {
        public static final String WALLET = "WALLET";
        public static final String CREDIT_CARD = "CREDIT_CARD";
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Float startValue;

    private Float currentValue;

    private String name;

    private String type; // WALLET or CREDIT_CARD

    public Account(int id, String name, float startValue, float currentValue, String type) {
        this.id = id;
        this.startValue = startValue;
        this.name = name;
        this.currentValue = currentValue;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStartValue(Float startValue) {
        this.startValue = startValue;
    }

    public Float getStartValue() {
        return startValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public Float getCurrentValue() {
        return currentValue;
    }

    public void setType(String type) {
        if (type == null || (!type.equals("CREDIT_CARD") && !type.equals("WALLET")))
            throw new IllegalArgumentException("Only CREDIT_CARD or WALLET, not \"" + type + "\"");
        this.type = type;
    }

    public String getType() {
            return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
