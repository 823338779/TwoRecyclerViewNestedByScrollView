package com.example.nestedrev

import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CjlAdapter : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400)
        }
        return object : ViewHolder(view) {}

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.itemView as? TextView)?.apply {
            text = "$position"
            setOnClickListener {
                Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return 30
    }
}