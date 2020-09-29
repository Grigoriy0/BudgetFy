package com.grigoriy0.budgetfy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDAO {
    @Insert
    void insertAll(Account... accounts);

    @Delete
    void delete(Account account);

    @Update
    void update(Account account);

    @Query("SELECT * FROM account_table")
    LiveData<List<Account>> getAllAccounts();
}
