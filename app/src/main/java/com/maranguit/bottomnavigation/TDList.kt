package com.maranguit.bottomnavigation

import android.net.Uri

data class TDList(
    var description: String,
    var isCompleted: Boolean = false,
    var imageUri: Uri? = null
)