package com.grigoriy0.budgetfy.accountdetails;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDAO {
    @Insert
    void insert(Transaction... accounts);

    @Delete
    void delete(Transaction account);

    @Update
    void update(Transaction account);

    @Query("SELECT * FROM transaction_table")
    List<Transaction> getAllTransactions();

    @Query("SELECT * FROM transaction_table WHERE accountId LIKE :id")
    List<Transaction> getByAccountId(int id);
}
