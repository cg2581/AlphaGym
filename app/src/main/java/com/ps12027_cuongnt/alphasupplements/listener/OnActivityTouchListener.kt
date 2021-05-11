package com.ps12027_cuongnt.alphasupplements.listener

import android.view.MotionEvent

interface OnActivityTouchListener {
    fun getTouchCoordinates(ev: MotionEvent?)
}