package com.example.calendarapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarapp.db.SimpleEvent


class ShowTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_task)

        val title = findViewById<TextView>(R.id.addtitle)
        val img = findViewById<ImageView>(R.id.img)
        val dataTxt = findViewById<TextView>(R.id.dataTxt)
        val startTxt = findViewById<TextView>(R.id.startTimeTxt)
        val endTxt = findViewById<TextView>(R.id.endTimeTxt)
        val info = findViewById<TextView>(R.id.info)


        findViewById<TextView>(R.id.cancelbtn).setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        val item : SimpleEvent = intent.getParcelableExtra("event")!!

        title.text = item.title
        dataTxt.text = item.date.toString()
        startTxt.text = item.start
        endTxt.text = item.end
        info.text = item.info

        if (item.place == TaskType.FRIENDS)
        {
            img.setBackgroundResource(R.drawable.friends)
        }

        else if (item.place == TaskType.JOB)
        {
            img.setBackgroundResource(R.drawable.job)
        }

        else if (item.place == TaskType.HOBBY)
        {
            img.setBackgroundResource(R.drawable.hobby)
        }

        else if (item.place == TaskType.HOME)
        {
            img.setBackgroundResource(R.drawable.house)
        }
    }

}