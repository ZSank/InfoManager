package com.app.infomanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.infomanager.data.models.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
	abstract fun itemDao(): ItemDao
}