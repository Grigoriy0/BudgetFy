package com.grigoriy0.budgetfy.accountdetails;

import com.grigoriy0.budgetfy.Category;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final UUID id;

    private final float sum;

    private final Date date;

    private final Category category;

    private boolean loss;

    public boolean isLoss() {
        return loss;
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

    public float getSum() {
        return sum;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return (new SimpleDateFormat("dd.MM.yyyy")).format(date);
    }

    public Category getCategory() {
        return category;
    }

    public UUID getId() {
        return id;
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
    public int hashCode() {
        return Objects.hash(id, sum, date, category);
    }

    public Transaction(float sum, Category category) {
        id = UUID.randomUUID();
        this.sum = sum;
        this.category = category;
        this.date = new Date(System.currentTimeMillis());
        this.loss = category.isLoss();
    }
}
