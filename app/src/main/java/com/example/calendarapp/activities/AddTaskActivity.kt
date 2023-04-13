package com.example.calendarapp.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.calendarapp.adapters.PlaceAdapter
import com.example.calendarapp.R
import com.example.calendarapp.TaskType
import com.example.calendarapp.db.Event
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private var placeList = mutableListOf<Place>()
    private lateinit var eventToAdd: Event
    private lateinit var eventList: kotlin.collections.ArrayList<Event>
    private lateinit var startTimeTxt: TextView
    private lateinit var endTimeTxt: TextView
    private lateinit var dateTxt: TextView
    private lateinit var taskName: TextView
    private lateinit var spinner: Spinner
    private lateinit var infoTxt: EditText
    private var position : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(resources.configuration.orientation== Configuration.ORIENTATION_LANDSCAPE) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // ukrycie tytułu aplikacji
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) // ustawienie trybu pełnoekranowego
        }
        setContentView(R.layout.activity_add_task)
        super.setTitle("Organize yourself")

        eventToAdd = intent.getParcelableExtra("event", Event::class.java)!!
        eventList = intent.getParcelableArrayListExtra("eventList", Event::class.java)!!
        position = intent.getIntExtra("position",0)


        val startTimeButton = findViewById<Button>(R.id.startTime)
        val endTimeButton = findViewById<Button>(R.id.endTime)
        startTimeTxt = findViewById(R.id.startTimeTxt)
        endTimeTxt = findViewById(R.id.endTimeTxt)
        infoTxt = findViewById(R.id.info)

        taskName = findViewById(R.id.taskName)


        val date = findViewById<Button>(R.id.data)
        dateTxt = findViewById(R.id.dataTxt)


        placeList.add(Place("Obowiązki domowe", R.drawable.home))
        placeList.add(Place("Praca", R.drawable.job))
        placeList.add(Place("Znajomi", R.drawable.friends))
        placeList.add(Place("Hobby", R.drawable.hobby))

        spinner = findViewById(R.id.spinner)

        val adatper = PlaceAdapter(this@AddTaskActivity, placeList)
        spinner.adapter = adatper


        startTimeButton.setOnClickListener{chooseStartTime()}
        endTimeButton.setOnClickListener{chooseEndTime()}
        date.setOnClickListener{datePicker()}

        findViewById<TextView>(R.id.cancelbtn).setOnClickListener{
            var myIntent = Intent()
            myIntent.putExtra("event", eventToAdd)
            myIntent.putExtra("position", position)

            setResult(Activity.RESULT_OK, myIntent)
            finish()
        }



        if (eventToAdd.title!="No title")
        {
            taskName.text = eventToAdd.title
            var id = 0
            if (eventToAdd.place == TaskType.HOME) id=0
            else if (eventToAdd.place == TaskType.JOB) id=1
            else if (eventToAdd.place == TaskType.FRIENDS) id=2
            else if (eventToAdd.place == TaskType.HOBBY) id=3

            spinner.setSelection(id)
            //spinner.setSelection(spinner.adapter.getItemId(eventToAdd.place))

            dateTxt.text =eventToAdd.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            startTimeTxt.text = eventToAdd.start
            endTimeTxt.text = eventToAdd.end
            infoTxt.text = Editable.Factory.getInstance().newEditable(eventToAdd.info)
        }
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

        var lista = eventList
            .filter { p -> p.date == LocalDate.parse(dateTxt.text,DateTimeFormatter.ofPattern("yyyy-MM-dd"));}


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
                if (item.start != eventToAdd.start && item.end != eventToAdd.end) {
                    MotionToast.createColorToast(
                        this,
                        getString(www.sanju.motiontoast.R.string.text_warning),
                        getString(R.string.bad_hours),
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                    return
                }
            }
        }


        if (eventToAdd.title=="No title") {
            MotionToast.createColorToast(
                this,
                getString(www.sanju.motiontoast.R.string.text_success),
                getString(R.string.success),
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }

        else
        {
            MotionToast.createColorToast(
                this,
                getString(www.sanju.motiontoast.R.string.text_success),
                getString(R.string.successUpdate),
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }



        var type : TaskType = if (spinner.selectedItem == 0) TaskType.HOME
        else if (spinner.selectedItem == 1) TaskType.JOB
        else if (spinner.selectedItem == 2) TaskType.FRIENDS
        else TaskType.HOBBY


        eventToAdd.date= LocalDate.parse(dateTxt.text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        eventToAdd.title = taskName.text.toString()

        eventToAdd.place = type
        eventToAdd.color = type.color
        eventToAdd.place.img = type.img

        eventToAdd.start = startTime.toString()
        eventToAdd.end = endTime.toString()
        eventToAdd.info = infoTxt.text.toString()



        myIntent.putExtra("event", eventToAdd)
        myIntent.putExtra("position", position)


        setResult(Activity.RESULT_OK, myIntent)
        finish()
    }
}


