package com.example.calendarapp.adapters


import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.R
import com.example.calendarapp.db.Event
import java.util.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.math.round


class CustomAdapter(
    private val data: ArrayList<Date>,
    private val currentDate: Calendar,
    private var eventMap: ArrayList<Event>
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>()  {

    private var mListener: OnItemClickListener? = null
    private lateinit var todaysEvents: List<Event>
    val calendar: Calendar = Calendar.getInstance(Locale("pl", "PL"))


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_calendar_day, parent, false)
        return ViewHolder(view, mListener!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("EEE dd", Locale("pl", "PL"))

        calendar.time = data[position]


        //ustawienie wyglądu dni
        holder.txtDay.setTextColor(Color.WHITE)
        holder.txtDayInWeek.setTextColor(Color.WHITE)
        holder.txtDay.setTypeface(null, Typeface.NORMAL)
        holder.txtDayInWeek.setTypeface(null, Typeface.NORMAL)

        holder.txtDay.textSize = 16F
        holder.progressBar.progress = 0

        //odfiltruj wszystkie taski z dzisiaj i oblicz dla nich progres
        todaysEvents = eventMap.filter { p -> p.date() == LocalDate.parse(LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId()).toLocalDate()
            .toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));}.toList()

        holder.progressBar.progress = round(countDayProgress(todaysEvents)).toInt()

        holder.txtDayInWeek.text = dateFormat.format(calendar.time).toString().split(" ")[0]
        holder.txtDay.text = calendar[Calendar.DAY_OF_MONTH].toString()



        //ustawienie na zielono dnia dzisiejszego
        if (calendar.time.toString() == currentDate.time.toString())
        {
            holder.txtDay.setTextColor(Color.GREEN)
            holder.txtDayInWeek.setTextColor(Color.GREEN)

            holder.txtDay.setTypeface(null, Typeface.BOLD)
            holder.txtDayInWeek.setTypeface(null, Typeface.BOLD)

            holder.txtDay.textSize = 18F
            holder.txtDay.textSize = 18F
        }

        var index: Int

        holder.linearLayout.setOnClickListener {
            index = holder.absoluteAdapterPosition
            holder.listener.onItemClick(index)
        }
    }

    private fun countDayProgress(simpleEvents: List<Event>): Float {

        var sum = 0.0F
        for (event in simpleEvents)
        {
            val startTime = LocalTime.parse(event.start, DateTimeFormatter.ISO_TIME)
            val endTime = LocalTime.parse(event.end, DateTimeFormatter.ISO_TIME)

            val hourDiff = endTime.hour - startTime.hour
            val minutesDiff = endTime.minute - startTime.minute

            sum += hourDiff + minutesDiff/60F
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

    fun updateEventList(eventList: ArrayList<Event>) {
        this.eventMap = eventList
    }

        override fun getItemCount(): Int {
        return data.size
    }
}