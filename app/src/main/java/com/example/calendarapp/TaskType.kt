package com.example.calendarapp

enum class TaskType(val color: Int, var img: Int) {
    HOME(R.color.tasktype1, R.drawable.home),
    JOB(R.color.tasktype2, R.drawable.job),
    FRIENDS(R.color.tasktype3, R.drawable.friends),
    HOBBY(R.color.tasktype4, R.drawable.hobby)
}
