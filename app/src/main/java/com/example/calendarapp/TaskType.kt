package com.example.calendarapp

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

enum class TaskType(val id: Int, val color: Int, var img: String) {
    HOME(0,R.color.tasktype1, "https://media.istockphoto.com/id/1145840259/vector/home-flat-icon-pixel-perfect-for-mobile-and-web.jpg?s=612x612&w=0&k=20&c=2DWK30S50TbctWwccYw5b-uR6EAksv1n4L_aoatjM9Q="),
    JOB(1,R.color.tasktype2, "https://zarabiajnapisaniu.com/wp-content/uploads/2018/06/jak-znalezc-prace-dodatkowa-w-domu-infografika.png"),
    FRIENDS(2,R.color.tasktype3, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Friends_logo.svg/2560px-Friends_logo.svg.png"),
    HOBBY(3,R.color.tasktype4, "https://img.freepik.com/free-vector/hand-drawn-flat-people-hobbies_23-2149053953.jpg?w=360");
}