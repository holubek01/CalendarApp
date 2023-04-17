package com.example.calendarapp.adapters


import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calendarapp.R
import com.example.calendarapp.db.Event
import com.example.calendarapp.db.EventDao
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class CustomAdapter2(
    private var eventMap: ArrayList<Event>,
    private var selectedDate: String,   //format yyyy-MM-dd
    var prevSelected: Int,
    var recyclerViewItemInterface: RecyclerViewItemInterface,
    var shouldAnimate: Boolean,
    private val itemDao: EventDao

) : RecyclerView.Adapter<CustomAdapter2.ViewHolder>() {

    private val periodHeight = 20
    private lateinit var todaysEvents:MutableList<Event>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return ViewHolder(view)
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun deleteItem(i: Int, viewHolder: RecyclerView.ViewHolder, adapter: CustomAdapter)
    {
        var state=true
        val item = todaysEvents[i]
        val idOfDeletedItem = todaysEvents[i].id
        eventMap.remove(item)
        if (eventMap.size!=0) todaysEvents.removeAt(i)

        GlobalScope.launch{        itemDao.deleteById(idOfDeletedItem)
        }



        Snackbar.make(viewHolder.itemView, "Usunięto: " + item.title, Snackbar.LENGTH_LONG).setBackgroundTint(
            Color.BLACK)
            .setTextColor(Color.WHITE)
            .setAction("COFNIJ") {
                state=false
                todaysEvents.add(item)
                eventMap.add(item)
                GlobalScope.launch {
                    itemDao.insertAll(item)
                }
                adapter.notifyDataSetChanged()
                this.notifyDataSetChanged()

            }.show()

        this.notifyDataSetChanged()
    }


    fun updateAdapterData(datetosee:String, prevSelected: Int, shouldAnimate: Boolean)
    {
        this.selectedDate = datetosee
        this.prevSelected = prevSelected
        this.shouldAnimate = shouldAnimate
        todaysEvents = eventMap.filter { p -> p.date() == LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"))}.sortedWith(compareBy<Event> { LocalTime.parse(it.start) }).toMutableList()
        notifyDataSetChanged()
    }

    fun setAnimation(shouldAnimate: Boolean)
    {
        this.shouldAnimate =shouldAnimate
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

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




        todaysEvents = eventMap.filter { p -> p.date() == LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"))}.sortedWith(compareBy<Event> { LocalTime.parse(it.start) }).toMutableList()

        val item = todaysEvents[position]
        if (item in todaysEvents)
        {
            val start = item.start
            val end = item.end

            val startTime = LocalTime.parse(start, DateTimeFormatter.ISO_TIME)
            val endTime = LocalTime.parse(end, DateTimeFormatter.ISO_TIME)

            val h = countTimeDifferece(endTime, startTime)

            val params = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams
            params.height = h


            Glide.with(holder.cardView)
                .load(item.place().img)
                .into(holder.img)


            params.topMargin = findLastTaskEndTime(startTime, todaysEvents)
            holder.cardView.layoutParams = params
            holder.task.text = item.title
            holder.cardView.setBackgroundResource(item.place()!!.color)
            holder.startDate.text = start
            holder.endDate.text = end


            if(h<=160)
            {
                holder.img.visibility = View.GONE
                holder.task.textSize = 20F
                holder.task.gravity = Gravity.CENTER
                holder.startDate.textSize = 15F
                holder.endDate.textSize = 15F
            }

            if (h<128)
            {
                holder.task.textSize = 16F
                holder.startDate.textSize = 12F
                holder.endDate.textSize = 12F
            }
            if (h<=90)
            {
                holder.task.textSize = 14F
                holder.startDate.visibility = View.GONE
                holder.endDate.visibility = View.GONE
            }
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
        val tmpList = eventMap.filter { p -> p.date() == LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"));}
        return tmpList.size
    }

    fun updateEventList(eventList: ArrayList<Event>) {
        this.eventMap = eventList
    }
}


