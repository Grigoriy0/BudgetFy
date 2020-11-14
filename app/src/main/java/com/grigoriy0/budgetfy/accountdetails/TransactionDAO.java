package com.grigoriy0.budgetfy.accountdetails;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

@Dao
public interface TransactionDAO {
    @Insert
    void insert(Transaction... transactions);

    @Delete
    void delete(Transaction transaction);

    @Query("DELETE FROM transaction_table WHERE accountId=:id")
    void deleteById(UUID id);

    @Update
    void update(Transaction transaction);

    @Query("SELECT * FROM transaction_table WHERE accountId=:id")
    LiveData<List<Transaction>> getByAccountId(UUID id);
}
