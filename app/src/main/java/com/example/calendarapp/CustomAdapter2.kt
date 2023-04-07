package com.example.calendarapp


import android.app.ActionBar.LayoutParams
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.db.SimpleEvent
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class CustomAdapter2(
    private val context: Context,
    private val data: ArrayList<String>,
    private val currentDate: Calendar,
    private val changeMonth: Boolean,
    private val eventMap: ArrayList<SimpleEvent>,
    private val selectedDay: String,
    val prevSelected: Int
) : RecyclerView.Adapter<CustomAdapter2.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (selectedDay!="")
        {

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



        val params = holder.task.getLayoutParams()
        params.height = LayoutParams.WRAP_CONTENT
        holder.task.setLayoutParams(params)
        holder.task.text = ""
        holder.border.visibility = View.VISIBLE
        holder.task.setBackgroundResource(R.color.white);




        holder.hour.visibility = View.VISIBLE
        holder.border.visibility = View.VISIBLE


        holder.task.clearComposingText()
        holder.hour.text = data[position]


        if (holder.hour.text.subSequence(3,5).toString().equals("15") || holder.hour.text.subSequence(3,5).toString().equals("45"))
        {
            holder.hour.visibility = View.INVISIBLE
            holder.border.visibility = View.INVISIBLE
        }


        if (selectedDay!="")
        {
            //if (eventMap.containsKey(selectedDay.toInt()))
            //{

            var temp = "${selectedDay.substring(0,4)}-${selectedDay.substring(4,6)}-${selectedDay.substring(6,8)}"
            var tmpList = eventMap.filter { p -> p.date == LocalDate.parse(temp,DateTimeFormatter.ofPattern("yyyy-MM-dd"));}

            //for (item in eventMap[selectedDay.toInt()]!!)
            for (item in tmpList)
            {
                var start = item.start
                var end = item.end
                var title = item.title

                var startTime = LocalTime.parse(start, DateTimeFormatter.ISO_TIME)
                var endTime = LocalTime.parse(end, DateTimeFormatter.ISO_TIME)
                var now = LocalTime.parse(holder.hour.text, DateTimeFormatter.ISO_TIME)


                if (holder.hour.text == start)
                {
                    val params = holder.task.getLayoutParams()
                    params.height = LayoutParams.MATCH_PARENT
                    holder.task.setLayoutParams(params)
                    holder.task.text = item.title
                    holder.task.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                    holder.border.setBackgroundResource(R.color.white);
                    holder.task.setBackgroundColor(item.place.color)
                }

                if ((now.isAfter(startTime) && now.isBefore(endTime)))
                {
                    val params = holder.task.getLayoutParams()
                    params.height = LayoutParams.MATCH_PARENT
                    holder.task.setLayoutParams(params)
                    holder.border.visibility = View.GONE
                    holder.task.setBackgroundColor(item.place.color)

                }
           }
            //}
        }




    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var hour: TextView = itemView.findViewById(R.id.txt_date)
        var task : TextView = itemView.findViewById(R.id.task_time)
        var border: View = itemView.findViewById(R.id.border)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}


