package com.grigoriy0.budgetfy.accountdetails;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.Category;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "transaction_table", foreignKeys = @ForeignKey(
        entity = Account.class,
        parentColumns = "id",
        childColumns = "accountId"
))
@TypeConverters({Transaction.DateConverter.class, Transaction.CategoryConverter.class})
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int accountId;

    private float sum;

    @TypeConverters({DateConverter.class})
    private Date date;

    @TypeConverters({CategoryConverter.class})
    private Category category;

    private boolean loss;

    public boolean isLoss() {
        return loss;
    }

    public int getAccountId() {
        return accountId;
    }

    public float getSum() {
        return sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setLoss(boolean loss) {
        this.loss = loss;
    }

    public String getDateString() {
        return (new SimpleDateFormat("dd.MM.yyyy")).format(date);
    }

    public Category getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Transaction(float sum, Category category, int accountId) {
        this.sum = sum;
        this.accountId = accountId;
        this.category = category;
        this.date = new Date(System.currentTimeMillis());
        this.loss = category.isLoss();
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
                ", category=" + getCategory() +
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
        public static Category toCategory(String category) {
            if (category == null)
                return null;
            switch (category) {
                case "TRANSPORT":
                    return Category.TRANSPORT;
                case "UNIVERSITY":
                    return Category.UNIVERSITY;
                case "FOOD":
                    return Category.FOOD;
                case "CAFE":
                    return Category.CAFE;
                case "PHONE":
                    return Category.PHONE;
                case "BARBER":
                    return Category.BARBER;
                case "RENT":
                    return Category.RENT;
                case "MISCELLANEOUS":
                    return Category.MISCELLANEOUS;
                case "SCHOLARSHIP":
                    return Category.SCHOLARSHIP;
                case "PARENTS":
                    return Category.PARENTS;
                case "GIFT":
                    return Category.GIFT;
                default:
                    return null;
            }
        }

        @TypeConverter
        public static String toString(Category category) {
            return category == null ? null : category.toString();
        }
    }
}
