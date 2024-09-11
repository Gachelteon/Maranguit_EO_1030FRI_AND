package com.maranguit.todolist

import android.net.Uri

data class Task(
    var description: String,
    var isCompleted: Boolean = false,
    var imageUri: Uri? = null
)

