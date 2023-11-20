package com.example.nestedrev

import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CjlAdapter : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SampleText(parent.context).apply {
            layoutParams = MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400).apply {
                setMargins(50, 100, 50, 100)
            }
            setBackgroundColor(Color.WHITE)
        }
        return object : ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.itemView as? SampleText)?.apply {
            text = "$position"
        }
    }

    override fun getItemCount(): Int {
        return 30
    }
}