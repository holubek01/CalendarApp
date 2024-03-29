package com.example.calendarapp.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.calendarapp.R
import com.example.calendarapp.db.Event


class ShowTaskActivity : AppCompatActivity() {

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

        setContentView(R.layout.activity_show_task)

        val title = findViewById<TextView>(R.id.addtitle)
        val img = findViewById<ImageView>(R.id.img)
        val dataTxt = findViewById<TextView>(R.id.dataTxt)
        val startTxt = findViewById<TextView>(R.id.startTimeTxt)
        val endTxt = findViewById<TextView>(R.id.endTimeTxt)
        val info = findViewById<TextView>(R.id.info)

        val selectedDay = intent.getIntExtra("selectedDay", 0)

        findViewById<TextView>(R.id.cancelbtn).setOnClickListener{
            val myIntent = Intent()
            myIntent.putExtra("selectedDay", selectedDay)
            setResult(Activity.RESULT_OK, myIntent)
            finish()
        }



        val item : Event = intent.getParcelableExtra("event", Event::class.java)!!

        title.text = item.title
        dataTxt.text = item.date().toString()
        startTxt.text = item.start
        endTxt.text = item.end
        info.text = item.info

        Glide.with(this)
            .load(item.place()!!.img)
            .into(img)
    }

}