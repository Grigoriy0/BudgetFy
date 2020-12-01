package com.grigoriy0.budgetfy;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "account_table")
public class Account implements Serializable {
    public static abstract class Type {
        public static final String WALLET = "WALLET";
        public static final String CREDIT_CARD = "CREDIT_CARD";
    }

    @NonNull
    @PrimaryKey
    @TypeConverters({UUIDConverter.class})
    public UUID id;

    public Float startValue;

    public Float currentValue;

    public String name;

    public String type; // WALLET or CREDIT_CARD

    public String currency;

    public Account(UUID id, String name, float startValue, String type, String currency) {
        this.id = id;
        this.currentValue = this.startValue = startValue;
        this.name = name;
        this.type = type;
        this.currency = currency;
    }

    public static class UUIDConverter {
        @TypeConverter
        public static UUID toUUID(String string) {
            return UUID.fromString(string);
        }

        @TypeConverter
        public static String toString(UUID uuid) {
            return uuid.toString();
        }
    }
}
