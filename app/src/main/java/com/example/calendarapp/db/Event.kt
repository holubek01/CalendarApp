package com.example.calendarapp.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calendarapp.TaskType
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


//przy zamianie trzeba będzie jedynie zmieniać datę place
@Parcelize
@Entity
class Event(
    @ColumnInfo(name = "dateString") var dateString: String?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "place") var place: String?,
    @ColumnInfo(name = "color") var color: Int?,
    @ColumnInfo(name = "start") var start:String?,
    @ColumnInfo(name = "end") var end: String?,
    @ColumnInfo(name = "info") var info: String?,
    @PrimaryKey(autoGenerate = true) var id:Int=0
) : Parcelable
{
    fun date(): LocalDate? =
        if(dateString == null) LocalDate.MIN else LocalDate.parse(dateString)

    fun start(): LocalTime =
        if(start == null) LocalTime.MIN else LocalTime.parse(start, DateTimeFormatter.ISO_TIME)


    fun place(): TaskType{
        if (place!!.contains("HOME")) return TaskType.HOME
        if (place!!.contains("JOB")) return TaskType.JOB
        if (place!!.contains("FRIENDS")) return TaskType.FRIENDS

        return TaskType.HOBBY
    }
}