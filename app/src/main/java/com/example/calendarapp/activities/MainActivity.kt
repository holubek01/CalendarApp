package com.example.calendarapp.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Bindable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.*
import com.example.calendarapp.adapters.CustomAdapter
import com.example.calendarapp.adapters.CustomAdapter2
import com.example.calendarapp.adapters.RecyclerViewItemInterface
import com.example.calendarapp.adapters.SwipeGesture
import com.example.calendarapp.db.Event
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity(), RecyclerViewItemInterface {


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
    private val dateFormat = SimpleDateFormat("M yyyy", Locale("pl", "PL"))
    private val dateFormat2 = SimpleDateFormat("yyyy-MM-dd", Locale("pl", "PL"))

    private lateinit var calendarView: RecyclerView
    private lateinit var eventView: RecyclerView
    private lateinit var eventToAdd : Event
    private lateinit var datetosee : String
    private lateinit var txtCurrentDate : TextView
    private lateinit var adapter: CustomAdapter
    private lateinit var adapter2: CustomAdapter2
    private lateinit var month:String
    private lateinit var year:String

    @Bindable
    private var eventList : ArrayList<Event> = arrayListOf()
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private var currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]
    private var selectedDay = currentDay
    private var selectedMonth = currentMonth
    private var selectedYear = currentYear
    private var prevSelectedDay = selectedDay
    private var shouldAnimate = true
    private var dates = ArrayList<Date>()


    //wyświetlanie szczegółów taska
    override fun onClick(position: Int) {
        val myIntent= Intent(this, ShowTaskActivity::class.java)
        val item = eventList[position]
        myIntent.putExtra("event", item)
        myIntent.putExtra("selectedDay", selectedDay)
        resultLauncher2.launch(myIntent)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("eventList", eventList)
        outState.putInt("selectedDay", selectedDay)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        eventList = savedInstanceState.getParcelableArrayList("eventList", Event::class.java)!!
        adapter2.updateEventList(eventList)
        adapter.updateEventList(eventList)


        adapter2.notifyDataSetChanged()
        adapter.notifyDataSetChanged()
        selectedDay = savedInstanceState.getInt("selectedDay")

        adapter2.notifyDataSetChanged()
        adapter.notifyDataSetChanged()
        txtCurrentDate.text = "$selectedDay $month $year"
        datetosee = dateFormat2.format(dates[selectedDay-1])

        adapter2.setAnimation(false)
        adapter2.updateAdapterData(datetosee, prevSelectedDay, shouldAnimate)

        calendarView.scrollToPosition(selectedDay-1)

        adapter.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
    }

    override fun onLongClick(position: Int) {
        val myIntent= Intent(this, AddTaskActivity::class.java)
        val item = eventList[position]
        myIntent.putExtra("event", item)
        myIntent.putExtra("eventList",eventList)
        myIntent.putExtra("position",position)
        resultLauncher3.launch(myIntent)
    }

    private var resultLauncher3 = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result -> val data = result.data
        val tmpEvent = data!!.getParcelableExtra("event", Event::class.java)!!

        //update taska
        eventList[data.getIntExtra("position",0)] = tmpEvent

        //cofnięcie do daty nowo utworzonego taska

        //narazie daje mozliwość updatowania także daty
        selectedDay = tmpEvent.date.dayOfMonth

        datetosee = dateFormat2.format(dates[selectedDay-1])

        adapter2.setAnimation(false)
        adapter2.updateAdapterData(datetosee, prevSelectedDay, shouldAnimate)

        txtCurrentDate.text = "$selectedDay $month $selectedYear"

        calendarView.scrollToPosition(selectedDay-1)


        adapter.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
    }

    private var resultLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result -> val data = result.data
        selectedDay = data!!.getIntExtra("selectedDay", 0)
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }

        setContentView(R.layout.activity_main)



        calendarView = findViewById(R.id.calendar_recycler_view)
        eventView = findViewById(R.id.calendar_recycler_view_2)
        txtCurrentDate = findViewById(R.id.txtCurrentDate)

        val eventListBackgrund = findViewById<LinearLayout>(R.id.mainLayout)
        eventListBackgrund.setBackgroundResource(R.drawable.bbbb)
        datetosee = dateFormat2.format(calendar.time)



        eventList.add(
            Event(
            date = LocalDate.of(2023,4,10),
            title = "Event 420",
            place = TaskType.FRIENDS,
            color = TaskType.FRIENDS.color,
            start = "00:00",
            end = "00:20",
            info = "jakies dodatkowe info"
        )
        )


        eventList.add(
            Event(
                date = LocalDate.of(2023,4,10),
                title = "Event 460",
                place = TaskType.JOB,
                color = TaskType.JOB.color,
                start = "03:00",
                end = "03:40",
                info = "jakies dodatkowe info"
            )
        )


        eventList.add(
            Event(
                date = LocalDate.of(2023,4,12),
                title = "Event 460",
                place = TaskType.HOME,
                color = TaskType.HOBBY.color,
                start = "05:00",
                end = "07:00",
                info = "jakies dodatkowe info"
            )
        )





        eventList.add(
            Event(
                date = LocalDate.of(2023,4,10),
                title = "Event 460",
                place = TaskType.HOBBY,
                color = TaskType.HOBBY.color,
                start = "11:00",
                end = "14:00",
                info = "jakies dodatkowe info"
            )
        )





        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(calendarView)

        setUpCalendar()
    }




    //changeMonth tylko wtedy gdy zmieniamy miesiąc na poprzedni lub następny
    private fun setUpCalendar(changeMonth: Calendar?=null) {

        month = months.getValue(dateFormat.format(calendar.time).split(" ")[0].toInt())
        year= dateFormat.format(calendar.time).split(" ")[1]
        txtCurrentDate.text = "$selectedDay $month $year"

        val monthCalendar = calendar.clone() as Calendar

        //liczba dni w aktualnym miesiącu
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //gdy zmieniamy miesiąc to ustalamy selectedDay na pierwszy dzień w miesiącu
        if (changeMonth!=null)
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
        dates.clear()

        //ustawiamy monthCalendar na 1 dzień w miesiącu
        monthCalendar.set(Calendar.DAY_OF_MONTH,1)

        //wczytujemy wszystkie dni z miesiąca (dodajemy do listy)
        while (dates.size < lastDay) {
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }






        if(resources.configuration.orientation== Configuration.ORIENTATION_PORTRAIT) {
            calendarView.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            eventView.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        else{
            calendarView.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            eventView.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

        adapter = CustomAdapter(dates, currentDate, eventList)
        calendarView.adapter = adapter

        if (currentMonth == selectedMonth) calendarView.scrollToPosition(currentDay-1)




        adapter2 = CustomAdapter2(eventList, datetosee, prevSelectedDay, this@MainActivity,shouldAnimate)
        eventView.adapter = adapter2

        val swipeGesture = object : SwipeGesture(this)
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (ItemTouchHelper.LEFT == direction)
                {
                    adapter2.setAnimation(false)
                    val pos = viewHolder.absoluteAdapterPosition
                    adapter2.deleteItem(pos)

                    //eventList.remove(eventList.filter { p -> p.date == LocalDate.parse(datetosee,
                      //  DateTimeFormatter.ofPattern("yyyy-MM-dd"))}.toMutableList()[pos])
                    adapter.notifyDataSetChanged()


                    //jeśli usuwany jest pierszy event to usuń margin top z kolejnego aby wyrównało się do góry


                    if (adapter2.itemCount>0 && pos == 0) {

                        if (eventView.findViewHolderForAdapterPosition(0)!=null) {
                            val params =
                                eventView.findViewHolderForAdapterPosition(0)!!.itemView.layoutParams as ViewGroup.MarginLayoutParams
                            params.topMargin = 0

                            //metoda która pozwala na 'dostanie się' do holder poza metodą onBindViewHolder
                            eventView.findViewHolderForAdapterPosition(0)!!.itemView.layoutParams =
                                params
                        }


                    }


                }

            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(eventView)





        adapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                prevSelectedDay = selectedDay
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]

                datetosee = dateFormat2.format(clickCalendar.time)

                adapter2.setAnimation(true)
                adapter2.updateAdapterData(datetosee, prevSelectedDay, shouldAnimate)
                txtCurrentDate.text = "$selectedDay $month $selectedYear"

            }
        })
    }


    fun nextMonth(view: View)
    {
        calendar.add(Calendar.MONTH, 1)

        val clickCalendar = Calendar.getInstance()
        clickCalendar.time = dates[0]
        prevSelectedDay = selectedDay
        selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
        datetosee = dateFormat2.format(clickCalendar.time)
        adapter2.setAnimation(true)
        adapter2.updateAdapterData(datetosee, prevSelectedDay, shouldAnimate)
        setUpCalendar(changeMonth = calendar)
    }

    fun prevMonth(view: View)
    {
        if (calendar.after(currentDate))
        {
            calendar.add(Calendar.MONTH, -1)

            val clickCalendar = Calendar.getInstance()
            clickCalendar.time = dates[0]
            prevSelectedDay = selectedDay
            selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
            datetosee = dateFormat2.format(clickCalendar.time)
            adapter2.setAnimation(true)
            adapter2.updateAdapterData(datetosee, prevSelectedDay, shouldAnimate)
            setUpCalendar(changeMonth = calendar)
        }
    }
    fun addTask(view: View) {

        //ciężko tak
        val addIntent= Intent(this, AddTaskActivity::class.java)

        //przekazujemy cały event i go nadpisujemy
        eventToAdd = Event()
        addIntent.putExtra("event",eventToAdd)
        addIntent.putExtra("eventList",eventList)



        //to moglibyśmy na spokojnie wysłać
        //addIntent.putExtra("event", eventMap[20230305]!![0])
        //addIntent.putExtra("event", arrayListOf(eventMap))

        resultLauncher.launch(addIntent)

    }



    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result -> val data = result.data
        val tmpEvent = data!!.getParcelableExtra("event", Event::class.java)!!
        eventList.add(tmpEvent)

        //cofnięcie do daty nowo utworzonego taska
        selectedDay = data.getParcelableExtra("event", Event::class.java)?.date!!.dayOfMonth

        datetosee = dateFormat2.format(dates[selectedDay-1])

        Toast.makeText(this@MainActivity, datetosee, Toast.LENGTH_SHORT).show()

        adapter2.setAnimation(false)
        adapter2.updateAdapterData(datetosee, prevSelectedDay, shouldAnimate)

        txtCurrentDate.text = "$selectedDay $month $selectedYear"

        calendarView.scrollToPosition(selectedDay-1)


        adapter.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
    }





}
