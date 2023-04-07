package com.example.calendarapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.calendarapp.db.SimpleEvent
//import kotlinx.android.synthetic.main.activity_add_task.*
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

//TODO: Po kliknięciu na taska wyświetlają się szczegóły
//(okno podobne do dodawania taska : nazwa, miejse, data, godzina,info)
//plus możliwość usunięcia

//aktywność powinna zwrócić wydarzenie (nazwe itp)

class AddTaskActivity : AppCompatActivity() {
    private var placeList = mutableListOf<Place>()
    private lateinit var event: SimpleEvent
    private lateinit var eventLList: kotlin.collections.ArrayList<SimpleEvent>
    private lateinit var startTimeTxt: TextView
    private lateinit var endTimeTxt: TextView
    private lateinit var dateTxt: TextView
    private lateinit var taskName: TextView
    private lateinit var spinner: Spinner
    private lateinit var infoTxt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        super.setTitle("Organize yourself")



        event = intent.getParcelableExtra("event")!!
        eventLList = intent.getParcelableArrayListExtra("eventList")!!


        val startTimeButton = findViewById<Button>(R.id.startTime)
        val endTimeButton = findViewById<Button>(R.id.endTime)
        startTimeTxt = findViewById<TextView>(R.id.startTimeTxt)
        endTimeTxt = findViewById<TextView>(R.id.endTimeTxt)
        infoTxt = findViewById<EditText>(R.id.info)

        val taskName = findViewById<TextView>(R.id.taskName)
        val taskType = findViewById<Spinner>(R.id.spinner)

        val date = findViewById<Button>(R.id.data)
        dateTxt = findViewById<TextView>(R.id.dataTxt)

        placeList.add(Place("Obowiązki domowe", R.drawable.house))
        placeList.add(Place("Praca", R.drawable.job))
        placeList.add(Place("Znajomi", R.drawable.friends))
        placeList.add(Place("Hobby", R.drawable.hobby))

        spinner = findViewById<Spinner>(R.id.spinner)

        val adatper = PlaceAdapter(this@AddTaskActivity, placeList)
        spinner.adapter = adatper


        startTimeButton.setOnClickListener{chooseStartTime()}
        endTimeButton.setOnClickListener{chooseEndTime()}
        date.setOnClickListener{datePicker()}



    }

    private fun chooseStartTime() {
        val cal= Calendar.getInstance()
        val timeSetListener= TimePickerDialog.OnTimeSetListener{ view, hour, minute->
            cal.set(Calendar.HOUR_OF_DAY,hour)
            cal.set(Calendar.MINUTE,minute)
            startTimeTxt.text= SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    //niestety nie mogę zrobić ograniczenia na interwały 15 minutowe
    private fun chooseEndTime() {

        val cal= Calendar.getInstance()


        val timeSetListener= TimePickerDialog.OnTimeSetListener{ view, hour, minute->
            cal.set(Calendar.HOUR_OF_DAY,hour)
            cal.set(Calendar.MINUTE,minute)
            endTimeTxt.text= SimpleDateFormat("HH:mm").format(cal.time)
        }




        val timer = TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
        timer.show()


    }

    fun datePicker() {
        val cal= Calendar.getInstance()
        val dateSetListener= DatePickerDialog.OnDateSetListener{ view, year, month, day ->
            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,month)
            cal.set(Calendar.DAY_OF_MONTH,day)
            dateTxt.text=SimpleDateFormat("yyyy-MM-dd").format(cal.time)
        }
        DatePickerDialog(this,dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
            Calendar.DAY_OF_MONTH)).show()
    }



    fun addTask(view: View) {
        val myIntent = Intent()
        myIntent.putExtra("tag", "hello from 2 activity")
        setResult(Activity.RESULT_OK, myIntent)



        finish()
    }

    inner class Place(val name: String, val img: Int) : java.io.Serializable

    fun backToCalendar(view: View) {
        finish()
    }
    fun saveTask(view: View) {
        val myIntent = Intent()
        //Nazwa wydarzenia nie może być pusta
        if(taskName.text!!.isEmpty())
        {
            MotionToast.createColorToast(this,getString(R.string.details),getString(R.string.emptyName),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            return
        }




        //data nie może być pusta
        if(dateTxt.text!!.isEmpty())
        {
            MotionToast.createColorToast(this,getString(R.string.details),getString(R.string.emptyDate),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            return
        }

        //początek i koniec nie mogą być puste
        if(startTimeTxt.text.isEmpty() || endTimeTxt.text.isEmpty())
        {
            MotionToast.createColorToast(this,getString(R.string.details),getString(R.string.emptyTime),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            return
        }

        //początek nie może być po końcu
        var startTime = LocalTime.parse(startTimeTxt.text, DateTimeFormatter.ISO_TIME)
        var endTime = LocalTime.parse(endTimeTxt.text, DateTimeFormatter.ISO_TIME)

        val hourDiff = endTime.hour - startTime.hour
        val minutesDiff = endTime.minute - startTime.minute
        val duration : Float = hourDiff + minutesDiff/60F

        if (startTime.isAfter(endTime) || duration == 0F)
        {
            MotionToast.createColorToast(this,getString(R.string.details),getString(R.string.startAfterEnd),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            return
        }

        //sprzawdzić czy nie ma już tasków w tym dniu o tej dacie

        var lista = eventLList
            .filter { p -> p.date == LocalDate.parse(dateTxt.text,DateTimeFormatter.ofPattern("yyyy-MM-dd"));}


        println(eventLList[0].date)
        //println(lista[0].title)

        //nie mogą nachodzić na siebie taski
        for (item in lista)
        {
            var start = LocalTime.parse(item.start, DateTimeFormatter.ISO_TIME)
            var end = LocalTime.parse(item.end, DateTimeFormatter.ISO_TIME)


            if ((startTime.isBefore(end) && endTime.isAfter(end)) ||      //zacyzna sie w srodku i zachodzi
                (startTime == start && endTime == end) ||             //te same taski
                (startTime.isAfter(start) && endTime.isBefore(end))||  //nowy task w środku starego
                (startTime.isBefore(start) && endTime.isAfter(start)) || //nowy task zaczyna sie przed starym ale zachcza o niego
                (startTime.isBefore(start) && endTime.isAfter(end)))
            {
                MotionToast.createColorToast(this,getString(www.sanju.motiontoast.R.string.text_warning), getString(R.string.bad_hours),
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                return
            }
        }

        MotionToast.createColorToast(this,getString(www.sanju.motiontoast.R.string.text_success),getString(R.string.success),
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))


        var type : TaskType
        if (spinner.selectedItem.toString() == "Obowiązki domowe")  type = TaskType.HOME
        else if (spinner.selectedItem.toString() == "Praca")  type = TaskType.JOB
        else if (spinner.selectedItem.toString() == "Znajomi")  type = TaskType.FRIENDS
        else type = TaskType.HOBBY


        event.date= LocalDate.parse(dateTxt.text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        event.title = taskName.text.toString()
        event.place = type
        event.color = type.color
        event.start = startTime.toString()
        event.end = endTime.toString()
        event.info = infoTxt.text.toString()

        myIntent.putExtra("event", event)

        setResult(Activity.RESULT_OK, myIntent)
        finish()

    }
}


