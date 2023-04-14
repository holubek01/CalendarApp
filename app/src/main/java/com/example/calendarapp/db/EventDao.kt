package com.example.calendarapp.db

import androidx.room.*

@Dao
interface EventDao {
    @Query("SELECT * FROM Event")
    fun getAll(): MutableList<Event>


    //jeśli użyjemy @insert to tak jak w Springu Room zrobi to za nas

    //Możemy użyć upsert (nowe), które jeśli wiersz istnieje
    //to go nadpisze a nie zwróci abort
    @Upsert
    suspend fun insertAll(vararg items: Event)

    @Delete
    suspend fun delete(item: Event)

    @Query("DELETE FROM Event")
    fun deleteAll()

    @Query("DELETE FROM Event WHERE id=:id")
    fun deleteById(id:Int)

    //@Query("INSERT INTO Event(date,title,place,color,start,end,info) VALUES(:title, :date, :place, :color, :start, :end, :info)")
    //fun insert(title:String, date: String, place:String, color:Int, start: String, end:String, info:String)
}


