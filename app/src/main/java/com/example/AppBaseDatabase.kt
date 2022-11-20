package com.example

import com.example.App.Companion.context
import androidx.room.Database
import com.example.bean.HistoryBean
import androidx.room.RoomDatabase
import com.example.AppBaseDatabase
import androidx.room.Room
import com.example.UserDao
import com.example.AppBaseDatabase.AppBaseDatabaseHolder

@Database(entities = [HistoryBean::class], version = 1, exportSchema = false)
abstract class AppBaseDatabase : RoomDatabase() {
    private object AppBaseDatabaseHolder {
        val instance = Room.databaseBuilder(context!!, AppBaseDatabase::class.java, "baseDb")
            .fallbackToDestructiveMigration().build()
    }

    abstract val historyDao: UserDao?

    companion object {
        val instance: AppBaseDatabase
            get() = AppBaseDatabaseHolder.instance
    }
}