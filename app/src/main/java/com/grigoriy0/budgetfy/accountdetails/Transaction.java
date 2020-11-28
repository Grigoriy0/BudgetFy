package com.grigoriy0.budgetfy.accountdetails;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.grigoriy0.budgetfy.Account;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "transaction_table", foreignKeys = @ForeignKey(
        entity = Account.class,
        parentColumns = "id",
        childColumns = "accountId",
        onDelete = ForeignKey.CASCADE
),
        indices = {@Index(value = {"accountId"})}
)
@TypeConverters({Transaction.DateConverter.class, Transaction.CategoryConverter.class, Account.UUIDConverter.class})
public class Transaction {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    @NonNull
    @PrimaryKey
    @TypeConverters({Account.UUIDConverter.class})
    public UUID id;

    @NonNull
    @TypeConverters({Account.UUIDConverter.class})
    public UUID accountId;

    public long sum;

    @TypeConverters({DateConverter.class})
    public Date date;

    @TypeConverters({CategoryConverter.class})
    public Category category;

    public boolean loss;

    public String comment;

    public Transaction(@NonNull Transaction other) {
        id = other.id;
        accountId = other.accountId;
        sum = other.sum;
        date = DateConverter.toDate(DateConverter.toTimestamp(other.date));
        category = other.category;
        loss = other.loss;
        comment = other.comment;
    }

    public Transaction(long sum, @NonNull Category category, String comment, @NonNull UUID accountId) {
        this.sum = sum;
        this.accountId = accountId;
        this.category = category;
        this.date = new Date(System.currentTimeMillis());
        this.loss = category.isLoss();
        this.comment = comment;
        id = UUID.randomUUID();
    }

    public String getDateString() {
        return DATE_FORMAT.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Float.compare(that.sum, sum) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                category == that.category;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sum=" + sum +
                ", date=" + date +
                ", category=" + category +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sum, date, category);
    }

    public static class DateConverter {
        @TypeConverter
        public static Date toDate(Long timestamp) {
            return timestamp == null ? null : new Date(timestamp);
        }

        @TypeConverter
        public static long toTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }

    public static class CategoryConverter {
        @TypeConverter
        public static Category toCategory(String string) {
            if (string == null)
                return null;
            switch (string) {
                case "Transport":
                    return Category.TRANSPORT;
                case "University":
                    return Category.UNIVERSITY;
                case "Food":
                    return Category.FOOD;
                case "Cafe":
                    return Category.CAFE;
                case "Phone":
                    return Category.PHONE;
                case "Barber":
                    return Category.BARBER;
                case "Rent":
                    return Category.RENT;
                case "Stipend":
                    return Category.STIPEND;
                case "Gift":
                    return Category.GIFT;
                case "Salary":
                    return Category.SALARY;
                case "Other":
                default:
                    return Category.OTHER;
            }
        }

        @TypeConverter
        public static String toString(Category category) {
            return category == null ? null : category.toString();
        }
    }
}
