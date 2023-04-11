package com.example.calendarapp


import android.app.ActionBar.LayoutParams
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.db.SimpleEvent
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class CustomAdapter2(
    private val context: Context,
    //private val data: ArrayList<String>,
    private val currentDate: Calendar,
    private val changeMonth: Boolean,
    private val eventMap: ArrayList<SimpleEvent>,
    private var selectedDay: String,

    val prevSelected: Int,
    var recyclerViewItemInterface: RecyclerViewItemInterface,
    var shouldAnimate: Boolean

) : RecyclerView.Adapter<CustomAdapter2.ViewHolder>()  {


    //5 min - 20 px
    private val periodsNumber = 288
    private val periodHeight = 20
    private var prev = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return ViewHolder(view)
    }

    fun deleteItem(position: Int)
    {
        eventMap.removeAt(position)
        prev=false

        //prev=selectedDay

        Toast.makeText(
            context,
            "eloeloeloelo",
            Toast.LENGTH_SHORT
        ).show()
        notifyDataSetChanged()
    }


    //jesli czas trwania jest <30 minut to ustaw inny layout
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (selectedDay!="")

        {


            if (shouldAnimate)
            {
                println("chuj")
                println(selectedDay)
                println("elo $prev")
                if (selectedDay.subSequence(6,8).toString().toInt() < prevSelected)
                {
                    var animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
                    holder.itemView.startAnimation(animation)
                }
                else
                {
                    var animation2 = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_right)
                    holder.itemView.startAnimation(animation2)
                }
            }

        }




        val params = holder.task.getLayoutParams()
        params.height = LayoutParams.WRAP_CONTENT
        holder.task.setLayoutParams(params)
        holder.task.text = ""
        //holder.task.setBackgroundResource(R.color.white);




        var item = eventMap[position]


        if (selectedDay!="")
        {

            var temp = "${selectedDay.substring(0,4)}-${selectedDay.substring(4,6)}-${selectedDay.substring(6,8)}"

            var tmpList = eventMap.filter { p -> p.date == LocalDate.parse(temp,DateTimeFormatter.ofPattern("yyyy-MM-dd"));}


            //lista wydarzen w danym dniu


            if (item in tmpList)
            {
                var start = item.start
                var end = item.end

                var startTime = LocalTime.parse(start, DateTimeFormatter.ISO_TIME)
                var endTime = LocalTime.parse(end, DateTimeFormatter.ISO_TIME)

                val hourDiff = endTime.hour - startTime.hour
                val minutesDiff = endTime.minute - startTime.minute

                //roznice trzeba zamienic na minuty, podzielic na 5 i pomnożyc przez wysokosc
                var diff = hourDiff*60 + minutesDiff

                var h = diff/5 * periodHeight

                holder.img.setBackgroundResource(R.drawable.friends)



                val params = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams
                params.height = h

                //jak znaleźć odpowiedni margin ?
                //funkcja szukająca poprzedniego taska (jego czasu zakończenia)


                params.topMargin = findLastTaskEndTime(startTime, tmpList)
                holder.cardView.layoutParams = params


                //val params2 = holder.task.layoutParams
                //params2.height = params.height
                //holder.task.layoutParams = params2

                holder.task.text = item.title
                //holder.task.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)

                holder.l.setBackgroundColor(item.place.color)

                holder.startDate.text = start
                holder.endDate.text = end


            }





            //}
        }




    }

    private fun findLastTaskEndTime(startTime: LocalTime?, tmpList: List<SimpleEvent>): Int {
        //var startTime = LocalTime.parse(start, DateTimeFormatter.ISO_TIME)


        //val latestEvent = eventMap.maxByOrNull { LocalTime.parse(it.end) }

        val maxEndTime = startTime
        val latestEventBeforeMaxEndTime = eventMap.filter {
            LocalTime.parse(it.end) < maxEndTime
        }.maxByOrNull {
            LocalTime.parse(it.end)
        }

        //Teraz obliczamy różnicę


        if (latestEventBeforeMaxEndTime!=null)
        {
            println(latestEventBeforeMaxEndTime.end)
            var firstEnd = LocalTime.parse(latestEventBeforeMaxEndTime!!.end, DateTimeFormatter.ISO_TIME)

            val hourDiff = startTime!!.hour - firstEnd.hour
            val minutesDiff = startTime!!.minute - firstEnd.minute

            var diff = hourDiff*60 + minutesDiff

            return diff/5 * periodHeight
        }

        return 0


    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var task : TextView = itemView.findViewById(R.id.title)
        var startDate : TextView = itemView.findViewById(R.id.startTime)
        var endDate : TextView = itemView.findViewById(R.id.endTime)
        var img : ImageView = itemView.findViewById(R.id.image)
        var cardView: CardView = itemView.findViewById(R.id.carView)
        var l: LinearLayout = itemView.findViewById(R.id.task_view_layout)

        init {
            itemView.setOnClickListener{
                //click.onLongClick(adapterPosition)
                recyclerViewItemInterface.onClick(adapterPosition)

            }
        }
    }






    override fun getItemCount(): Int {

        //println("eloelo $selectedDay")

        //jesli nie jets wybrany to wybierz pierwszy dzien
        if (selectedDay=="")
        {
            val currentDate = Calendar.getInstance(Locale.ENGLISH)

            val currentDay = currentDate[Calendar.DAY_OF_MONTH]
            val currentMonth = currentDate[Calendar.MONTH]
            val currentYear = currentDate[Calendar.YEAR]

            var day2 = ""
            if (currentDay<10) day2 = "0$currentDay"
            else day2 = "$currentDay"

            var month2 = ""
            var tmp = currentMonth+1
            if (tmp<10) month2 = "0$tmp"
            else month2 = "$tmp"

            var temp = "$currentYear-$month2-$day2"
            var tmpList = eventMap.filter { p -> p.date == LocalDate.parse(temp,DateTimeFormatter.ofPattern("yyyy-MM-dd"));}

            return tmpList.size
        }
        else
        {
            var temp = "${selectedDay.substring(0,4)}-${selectedDay.substring(4,6)}-${selectedDay.substring(6,8)}"
            var tmpList = eventMap.filter { p -> p.date == LocalDate.parse(temp,DateTimeFormatter.ofPattern("yyyy-MM-dd"));}
            return tmpList.size
        }

    }
}


