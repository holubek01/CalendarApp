package com.example.calendarapp


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.db.SimpleEvent
import java.util.*

//import kotlinx.android.synthetic.main.custom_calendar_day.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.math.round


class CustomAdapter(
    private val context: Context,
    private val data: ArrayList<Date>,
    private val currentDate: Calendar,
    private val changeMonth: Calendar?,
    private var eventmap: ArrayList<SimpleEvent>,
    private var selectedDayy: Int
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>()  {

    private var mListener: OnItemClickListener? = null
    var eventMap = eventmap

    //mapa przechowująca liczbę eventów w danym dniu

    private var selectCurrentDate = true

    val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    val currentMonth = currentDate[Calendar.MONTH]
    val currentYear = currentDate[Calendar.YEAR]
    private val selectedDay =
        when {
            changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
            else -> currentDay
        }
    private val selectedMonth =
        when {
            changeMonth != null -> changeMonth[Calendar.MONTH]
            else -> currentMonth
        }
    private val selectedYear =
        when {
            changeMonth != null -> changeMonth[Calendar.YEAR]
            else -> currentYear
        }



    //tworzy widoki, musimy przekazać mu z czego ma tworzyć

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_calendar_day, parent, false)
        return ViewHolder(view, mListener!!)
    }




    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("EEE dd", Locale("pl", "PL"))
        val calendar = Calendar.getInstance(Locale("pl", "PL"))
        calendar.time = data[position]

        holder.txtDay.setTextColor(Color.BLACK)
        holder.txtDayInWeek.setTextColor(Color.BLACK)
        holder.txtDay.setTypeface(null, Typeface.NORMAL)
        holder.txtDayInWeek.setTypeface(null, Typeface.NORMAL)

        holder.txtDay.setTextSize(12F)




        holder.progressBar.progress = 0

        var tmpList = eventMap.filter { p -> p.date == LocalDate.parse(LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate()
            .toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));}.toList()

        holder.progressBar.progress =
                round(countDayProgress(tmpList)).toInt() //eventMap[tmp]!!.progress

        holder.txtDayInWeek.text = dateFormat.format(calendar.time).toString().split(" ")[0]
        holder.txtDay.text = calendar[Calendar.DAY_OF_MONTH].toString()




        if (calendar.time.toString() == currentDate.time.toString())
        {
            holder.txtDay.setTextColor(Color.GREEN)
            holder.txtDayInWeek.setTextColor(Color.GREEN)

            holder.txtDay.setTypeface(null, Typeface.BOLD)
            holder.txtDayInWeek.setTypeface(null, Typeface.BOLD)

            holder.txtDay.setTextSize(14F)
            holder.txtDay.setTextSize(14F)
        }

        var index = 0
        holder.linearLayout!!.setOnClickListener {
            index = holder.adapterPosition
            selectCurrentDate = false
            holder.listener.onItemClick(index)

        }


    }




    private fun countDayProgress(simpleEvents: List<SimpleEvent>): Float {

        var sum:Float = 0.0F
        if (simpleEvents != null) {

            for (event in simpleEvents)
            {
                var startTime = LocalTime.parse(event.start, DateTimeFormatter.ISO_TIME)
                var endTime = LocalTime.parse(event.end, DateTimeFormatter.ISO_TIME)

                val hourDiff = endTime.hour - startTime.hour
                val minutesDiff = endTime.minute - startTime.minute


                sum += hourDiff + minutesDiff/60F


            }
        }

        return sum * 100/24



    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }



    class ViewHolder(itemView: View, val listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        var txtDay : TextView = itemView.findViewById(R.id.txt_date)
        var txtDayInWeek : TextView = itemView.findViewById(R.id.txt_day)
        var linearLayout : FrameLayout= itemView.findViewById(R.id.calendar_linear_layout)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBarCalenderItemViewPager)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}


