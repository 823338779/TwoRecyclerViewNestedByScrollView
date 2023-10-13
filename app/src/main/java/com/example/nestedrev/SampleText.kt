package com.example.nestedrev

import android.content.Context
import android.widget.TextView
import android.widget.Toast

class SampleText(context: Context): androidx.appcompat.widget.AppCompatTextView(context) {
    init {
        setOnClickListener {
            Toast.makeText(context, "$text", Toast.LENGTH_SHORT).show()
        }
    }
}