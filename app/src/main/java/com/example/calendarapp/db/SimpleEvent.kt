package com.example.calendarapp.db

import android.graphics.Color
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calendarapp.AddTaskActivity
import com.example.calendarapp.TaskType
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime


@Parcelize
//@Entity
class SimpleEvent(var date: LocalDate, var title: String, var place: TaskType,
                  var color: Int, var start: String, var end: String, var info: String) : Parcelable
/*@PrimaryKey(autoGenerate = true) val id:Int = 0,
@ColumnInfo(name = "date") var dateString: String?,
@ColumnInfo(name = "title") var title: String,
@ColumnInfo(name = "place") var place: String?,
@ColumnInfo(name = "color") var color: Int,
@ColumnInfo(name = "start") var startString:String,
@ColumnInfo(name = "end") var endString: String)

 */
    //@ColumnInfo(name = "info")var info: String) : Parcelable

