package com.example.nestedrev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.nested.CjlAdapter
import com.example.nested.CjlNest

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
            setUpRev(rev11, rev22)
            llv = findViewById(R.id.container)
        }
    }
}