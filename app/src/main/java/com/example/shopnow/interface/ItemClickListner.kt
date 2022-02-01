package com.example.shopnow.`interface`

import android.view.View

interface ItemClickListner {
    fun onClick(view : View, position:Int, isLongClick:Boolean)
}