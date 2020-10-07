package com.grigoriy0.budgetfy.accountdetails;

import androidx.lifecycle.LiveData;
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

    @Query("DELETE FROM transaction_table WHERE accountId=:id")
    void deleteById(int id);

    @Update
    void update(Transaction account);

    @Query("SELECT * FROM transaction_table WHERE accountId=:id")
    LiveData<List<Transaction>> getByAccountId(int id);
}
