package com.example.calendarapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.R
import com.example.calendarapp.db.Event
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class CustomAdapter2(
    private var eventMap: ArrayList<Event>,
    private var selectedDate: String,   //format yyyy-MM-dd
    var prevSelected: Int,
    var recyclerViewItemInterface: RecyclerViewItemInterface,
    var shouldAnimate: Boolean

) : RecyclerView.Adapter<CustomAdapter2.ViewHolder>() {


    //5 min - 20 px
    private val periodHeight = 20
    private lateinit var todaysEvents:MutableList<Event>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return ViewHolder(view)
    }


    fun deleteItem(i: Int)
    {
        //nie może być removeAt bo są dodawane nie po kolei
        //eventMap.removeAt(i)

        var pos = eventMap.indexOf(todaysEvents[i])
        eventMap.remove(todaysEvents[i])
        if (eventMap.size!=0) todaysEvents.removeAt(i)
        this.notifyItemRemoved(pos)
    }


    fun updateAdapterData(datetosee:String, prevSelected: Int, shouldAnimate: Boolean)
    {
        this.selectedDate = datetosee
        this.prevSelected = prevSelected
        this.shouldAnimate = shouldAnimate
        todaysEvents = eventMap.filter { p -> p.date == LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"))}.toMutableList()
        notifyDataSetChanged()
    }

    fun setAnimation(shouldAnimate: Boolean)
    {
        this.shouldAnimate =shouldAnimate
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //po usunięciu wcale onBind.. się nie wykonuje
        if (shouldAnimate)
        {

            val selectedDay = LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("yyyy-MM-dd")).dayOfMonth

            //ustaw animacje w zależności od wybranego dnia
            if (selectedDay < prevSelected)
            {
                val animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
                holder.itemView.startAnimation(animation)
            }
            else
            {
                val animation2 = AnimationUtils.loadAnimation(holder.itemView.context,
                    R.anim.slide_in_right
                )
                holder.itemView.startAnimation(animation2)
            }
        }




        todaysEvents = eventMap.filter { p -> p.date == LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"))}.toMutableList()

        val item = todaysEvents[position]
        if (item in todaysEvents)
        {
            val start = item.start
            val end = item.end

            val startTime = LocalTime.parse(start, DateTimeFormatter.ISO_TIME)
            val endTime = LocalTime.parse(end, DateTimeFormatter.ISO_TIME)

            val h = countTimeDifferece(endTime, startTime)

            holder.img.setBackgroundResource(item.place.img)

            val params = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams
            params.height = h

            //jak znaleźć odpowiedni margin ?
            //funkcja szukająca poprzedniego taska (jego czasu zakończenia)


            params.topMargin = findLastTaskEndTime(startTime, todaysEvents)
            holder.cardView.layoutParams = params
            holder.task.text = item.title
            holder.cardView.setBackgroundResource(item.place.color)
            //holder.cardView.setBackgroundColor(item.place.color)
            //holder.cardView.setCardBackgroundColor(item.place.color)
            holder.startDate.text = start
            holder.endDate.text = end
        }
    }

    private fun findLastTaskEndTime(startTime: LocalTime?, todaysEvents: List<Event>): Int {

        val latestEventBeforeMaxEndTime = todaysEvents.filter {
            LocalTime.parse(it.end) < startTime
        }.maxByOrNull {
            LocalTime.parse(it.end)
        }

        //Teraz obliczamy różnicę
        if (latestEventBeforeMaxEndTime!=null)
        {
            val firstEnd = LocalTime.parse(latestEventBeforeMaxEndTime.end, DateTimeFormatter.ISO_TIME)
            return countTimeDifferece(startTime, firstEnd)
        }

        return 0


    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var task : TextView = itemView.findViewById(R.id.title)
        var startDate : TextView = itemView.findViewById(R.id.startTime)
        var endDate : TextView = itemView.findViewById(R.id.endTime)
        var img : ImageView = itemView.findViewById(R.id.image)
        var cardView: CardView = itemView.findViewById(R.id.carView)


        init {
            itemView.setOnClickListener{
                recyclerViewItemInterface.onClick(eventMap.indexOf(todaysEvents[absoluteAdapterPosition]))
            }

            itemView.setOnLongClickListener{
                recyclerViewItemInterface.onLongClick(eventMap.indexOf(todaysEvents[absoluteAdapterPosition]))
                true
            }


        }


    }

    private fun countTimeDifferece(startTime: LocalTime?, firstEnd: LocalTime):Int
    {

        val hourDiff = startTime!!.hour - firstEnd.hour
        val minutesDiff = startTime.minute - firstEnd.minute

        val diff = hourDiff*60 + minutesDiff

        return diff/5 * periodHeight
    }


    override fun getItemCount(): Int {
        val tmpList = eventMap.filter { p -> p.date == LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"));}
        return tmpList.size
    }

    fun updateEventList(eventList: ArrayList<Event>) {
        this.eventMap = eventList

        /*
        for (item in eventMap)
        {
            if (!eventMap.contains(item))
            {
                deleteItem(eventList.indexOf(it))
            }
        }

         */
    }
}


