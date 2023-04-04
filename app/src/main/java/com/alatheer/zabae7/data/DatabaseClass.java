package com.alatheer.zabae7.data;

import android.content.Context;

import com.alatheer.zabae7.home.product.OrderItemList;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
@Database(entities = {OrderItemList.class},version = 6,exportSchema = false)
public abstract class DatabaseClass extends RoomDatabase {
    public abstract Dao getDao();

}
