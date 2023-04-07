package com.example.calendarapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.db.SimpleEvent
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


//TODO po kliknięciu na taska pojawiają się szczegóły
//TODO usuwanie taska
//TODO: Landscape
//TODO: On restore data

class MainActivity : AppCompatActivity() {


    private val months = mapOf(1 to "Styczeń", 2 to "Luty",
        3 to "Marzec" ,
        4 to "Kwiecień",
        5 to "Maj",
        6 to "Czerwiec",
        7 to "Lipiec",
        8 to "Sierpień",
        9 to "Wrzesień",
        10 to "Październik",
        11 to "Listopad",
        12 to "Grudzień")

    private val calendar = Calendar.getInstance(Locale("pl", "PL"))
    private val calendarCurrent = Calendar.getInstance(Locale("pl", "PL"))

    //miesiąc rok
    private val dateFormat = SimpleDateFormat("M yyyy", Locale("pl", "PL"))

    private lateinit var calendarView: RecyclerView
    private lateinit var calendarView2: RecyclerView
    private lateinit var prevBtn : Button
    private lateinit var nextBtn : Button
    private lateinit var eventToAdd : SimpleEvent
    private lateinit var datetosee : String

    private lateinit var txt_current_month : TextView



    //lista SimpleEvents
    private var eventList : ArrayList<SimpleEvent> = arrayListOf()

    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    private var selectedDay = currentDay
    private var selectedMonth = currentMonth
    private var selectedYear = currentYear
    private var prevSelected = selectedDay

    private var dates = java.util.ArrayList<Date>()
    private var hours = java.util.ArrayList<String>()
    //private val eventMap: MutableMap<Int, MutableList<SimpleEvent>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.setTitle("Organize yourself")





        calendarView = findViewById(R.id.calendar_recycler_view)
        calendarView2 = findViewById(R.id.calendar_recycler_view_2)
        prevBtn = findViewById(R.id.prev_month_button)
        nextBtn = findViewById(R.id.next_month_button)
        txt_current_month = findViewById(R.id.txt_current_month)


        eventList.add(
            SimpleEvent(
            date = LocalDate.of(2023,3,5),
            title = "Event 420",
            place = TaskType.HOME,
            color = TaskType.HOBBY.color,
            start = "00:00",
            end = "02:00",
            info = "jakies dodatkowe info"
        )
        )


        //nie można się cofnąć się do wcześniejszego miesiąca niż obecna data
        prevBtn.setOnClickListener{
            if (calendar.after(currentDate))
            {
                calendar.add(Calendar.MONTH, -1)
                setUpCalendar(changeMonth = calendar)
            }

        }

        nextBtn.setOnClickListener{
            calendar.add(Calendar.MONTH, 1)
            setUpCalendar(changeMonth = calendar)

        }







        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(calendarView)

        //calendarLastDayInCalendar.add(Calendar.MONTH, 6)

        setUpCalendar()
    }


    //changeMonth tylko wtedy gdy zmieniamy miesiąc na poprzedni lub następny
    private fun setUpCalendar(changeMonth: Calendar?=null) {

        //ustawia text view na aktualny miesiac
        //samo dateFormat.toString wyswietli nam dziwne liczby


        val month = months.getValue(dateFormat.format(calendar.time).split(" ")[0].toInt())

        val year = dateFormat.format(calendar.time).split(" ")[1]
        txt_current_month.text = "$month $year"


        val monthCalendar = calendar.clone() as Calendar

        //liczba dni w aktualnym miesiącu
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //gdy zmieniamy miesiąc to ustalamy selectedDay na pierwszy dzień w miesiącu
        /*if (changeMonth!=null)
        {
            selectedDay = changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
            selectedMonth = changeMonth[Calendar.MONTH]
            selectedYear = changeMonth[Calendar.YEAR]
        }
        else
        {
            selectedDay = currentDay
            selectedMonth = currentMonth
            selectedYear = currentYear
        }

         */
        selectedDay =
            when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }
        selectedMonth =
            when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }
        selectedYear =
            when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }


        //wymazujemy daty
        var currentPosition = 0

        dates.clear()

        //ustawiamy monthCalendar na 1 dzień w miesiącu
        monthCalendar.set(Calendar.DAY_OF_MONTH,1)

        //wczytujemy wszystkie dni z miesiąca (dodajemy do listy)




        while (dates.size < lastDay) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }


        when {
            currentPosition > 2 -> calendarView!!.scrollToPosition(currentPosition - 3)
            lastDay - currentPosition < 2 -> calendarView!!.scrollToPosition(currentPosition)
            else -> calendarView!!.scrollToPosition(currentPosition)
        }


        calendarView.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var adapter = CustomAdapter(this, dates, currentDate, changeMonth, eventList, 0)
        calendarView.adapter = adapter

        calendarView2.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false, )


        for (i in 0 until 24)
        {
            if (i<10)
            {
                hours.add("0$i:00")
                hours.add("0$i:15")
                hours.add("0$i:30")
                hours.add("0$i:45")
            }
            else {
                hours.add("$i:00")
                hours.add("$i:15")
                hours.add("$i:30")
                hours.add("$i:45")
            }

        }


        calendarView2.adapter = CustomAdapter2(
            this,
            hours,
            currentDate,
            false,
            eventList,
            "",
            0
        )



        //zmienia selcted day


        adapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                prevSelected = selectedDay
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]


                Toast.makeText(
                    this@MainActivity,
                    "Selected day: $selectedDay ",
                    Toast.LENGTH_SHORT
                ).show()


                var day2 = ""
                if (selectedDay<10) day2 = "0$selectedDay"
                else day2 = "$selectedDay"

                var month2 = ""
                var tmp = selectedMonth+1
                if (tmp<10) month2 = "0$tmp"
                else month2 = "$tmp"


                datetosee = "${selectedYear}${month2}${day2}"


                calendarView2.adapter = CustomAdapter2(this@MainActivity,hours,currentDate,false, eventList, datetosee, prevSelected)

                txt_current_month.text = "$selectedDay $month $selectedYear"

            }


        })
    }

    fun addTask(view: View) {
/*
        val imageUris: ArrayList<SimpleEvent> = arrayListOf(
            eventMap[20230305]!![0],
            eventMap[20230305]!![1]
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
            type =
        }
        startActivity(Intent.createChooser(shareIntent, null))

 */



        //ciężko tak
        val addIntent= Intent(this,AddTaskActivity::class.java)

        eventToAdd = SimpleEvent(
            date = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate(),
            title = "Event 1",
            place = TaskType.HOME,
            color = ContextCompat.getColor(this, R.color.teal_700),
            start = "08:00",
            end = "11:30",
            info = "jakies dodatkowe info"
        )
        addIntent.putExtra("event",eventToAdd)
        addIntent.putExtra("eventList",eventList)



        //to moglibyśmy na spokojnie wysłać
        //addIntent.putExtra("event", eventMap[20230305]!![0])
        //addIntent.putExtra("event", arrayListOf(eventMap))

        resultLauncher.launch(addIntent)

    }



    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result -> val data = result.data
        eventList.add(data!!.getParcelableExtra("event")!!)
        calendarView2.adapter = CustomAdapter2(this@MainActivity,hours,currentDate,false, eventList, "", prevSelected)


    }
}