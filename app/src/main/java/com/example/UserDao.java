package com.example;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bean.HistoryBean;

import java.util.List;

@Dao
    public interface UserDao {
        @Query("SELECT * FROM historybean")
        List<HistoryBean> getAll();



        @Insert
        void insert(HistoryBean user);

    }