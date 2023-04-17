package com.example.calendarapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.calendarapp.R
import com.example.calendarapp.activities.AddTaskActivity


class PlaceAdapter(private val contect: Context, private val placelist: MutableList<AddTaskActivity.Place>) : BaseAdapter() {

    class ItemHolder(view: View){
        private val txtName:TextView
        val img:ImageView

        init{
            txtName = view.findViewById(R.id.name)
            img = view.findViewById(R.id.image)

        }
    }
    override fun getCount(): Int {
        return placelist.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(contect).inflate(R.layout.item_place, parent, false)
        val txtName = view.findViewById<TextView>(R.id.name)

        val viewHolder= ItemHolder(view)
        txtName.text = placelist[position].name

        Glide.with(view)
            .load(placelist[position].img)
            .into(viewHolder.img)

        return view
    }


}