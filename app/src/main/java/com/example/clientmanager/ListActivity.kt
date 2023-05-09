package com.example.clientmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val rvList: RecyclerView = findViewById(R.id.rv_list)

        val adapter = ListAdapter()
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)
    }
}