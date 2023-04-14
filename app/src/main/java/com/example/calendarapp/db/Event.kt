package com.example.calendarapp.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calendarapp.TaskType
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate


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
    @PrimaryKey(autoGenerate = true) val id:Int=0
) : Parcelable
{
    fun date(): LocalDate? =
        if(dateString == null) LocalDate.MIN else LocalDate.parse(dateString)



    fun place(): TaskType? = if(place == null) TaskType.HOME else TaskType.HOBBY
}
/*
        var date: LocalDate = LocalDate.MIN,
        var title : String = "No title",
        var place: TaskType = TaskType.HOME,
        var color: Int = TaskType.HOME.color,
        var start: String = "-1",
        var end: String = "-1",
        var info: String = "Brak dodatkowych informacji"

 */

/*@PrimaryKey(autoGenerate = true) val id:Int = 0,
@ColumnInfo(name = "date") var dateString: String?,
@ColumnInfo(name = "title") var title: String,
@ColumnInfo(name = "place") var place: String?,
@ColumnInfo(name = "color") var color: Int,
@ColumnInfo(name = "start") var startString:String,
@ColumnInfo(name = "end") var endString: String)

 */
    //@ColumnInfo(name = "info")var info: String) : Parcelable

