package com.ar.reminder.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ar.reminder.model.ListResponseModel
import com.ar.reminder.utils.Converters

@Database(entities = [ListResponseModel.ResponseModelItem::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun responseModelDao(): ListDao
}