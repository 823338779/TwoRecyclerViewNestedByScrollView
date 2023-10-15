package com.example.nestedrev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rev11 = findViewById<RecyclerView>(R.id.rev1).apply {
            adapter = CjlAdapter()
        }
        val rev22 = findViewById<RecyclerView>(R.id.rev2).apply {
            adapter = CjlAdapter()
        }
        findViewById<CjlNest>(R.id.cjl_parent).apply {
            llv = this@MainActivity.findViewById(R.id.container)
            setUpRev(rev11, rev22)
        }

    }
}