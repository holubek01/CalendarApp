/*package com.example.calendarapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM SimpleEvent")
    fun getAll(): MutableList<SimpleEvent>


    //jeśli użyjemy @insert to tak jak w Springu Room zrobi to za nas

    //Możemy użyć upsert (nowe), które jeśli wiersz istnieje
    //to go nadpisze a nie zwróci abort
    //@Insert
    //suspend fun insertAll(vararg items: SimpleEvent)

    @Delete
    suspend fun delete(item: SimpleEvent)

    @Query("DELETE FROM SimpleEvent")
    fun deleteAll()

    @Query("DELETE FROM SimpleEvent WHERE id=:id")
    fun deleteById(id:Int)

    //tutaj nadpisujemy inserta
    @Query("INSERT INTO SimpleEvent(date,title,place,color,start,end,info) VALUES(:title, :date, :place, :color, :start, :end, :info)")
    fun insert(title:String, date: String, place:String, color:Int, start: String, end:String, info:String)
}

 */
