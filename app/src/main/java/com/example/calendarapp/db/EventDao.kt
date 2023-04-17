package com.example.calendarapp.db

import androidx.room.*
import java.time.LocalTime

@Dao
interface EventDao {
    @Query("SELECT * FROM Event")
    fun getAll(): MutableList<Event>

    @Query("SELECT * FROM Event ORDER BY id DESC LIMIT 1")
    fun findLatestTask(): Event


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg items: Event)

    @Delete
    suspend fun delete(item: Event)

    @Query("DELETE FROM Event")
    fun deleteAll()

    @Query("DELETE FROM Event WHERE id=:id")
    fun deleteById(id:Int)

    //zwraca eventy których czas rozpoczęcia mieści się w zakresie 2 minut
    @Query("SELECT * FROM Event WHERE time(start)>=time('now', '+2 hours') and time(start)<=time('now', '+2 hours', '+2 minutes')and date('now')==date(dateString)")
    fun toNotify():MutableList<Event>

}


