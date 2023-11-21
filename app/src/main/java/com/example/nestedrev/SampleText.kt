package com.example.nestedrev

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.widget.Toast

class SampleText(context: Context): androidx.appcompat.widget.AppCompatTextView(context) {
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    init {
        setOnClickListener {
            Toast.makeText(context, "$text", Toast.LENGTH_SHORT).show()
        }
        setOnLongClickListener {
            it.setBackgroundColor(Color.CYAN)
            true
        }
    }
}