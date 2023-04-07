package com.example.calendarapp

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class PlaceAdapter(private val contect: Context, val placelist: MutableList<AddTaskActivity.Place>) : BaseAdapter() {

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
        val view = LayoutInflater.from(contect).inflate(com.example.calendarapp.R.layout.item_place, parent, false)
        val txtName = view.findViewById<TextView>(com.example.calendarapp.R.id.name)
        val img = view.findViewById<ImageView>(com.example.calendarapp.R.id.image)

        txtName.text = placelist.get(position).name
        img.setImageResource(placelist.get(position).img)

        return view

    }


}