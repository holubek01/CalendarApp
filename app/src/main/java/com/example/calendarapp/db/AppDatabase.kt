package com.example.calendarapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Event::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    //abstract var dao: EventDao
    abstract fun itemDao(): EventDao
}


