package com.example.clientmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientmanager.databinding.ActivityListBinding
import com.example.clientmanager.databinding.ActivityMainBinding

class ListActivity : AppCompatActivity() {

    lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rvList: RecyclerView = findViewById(R.id.rv_list)

        binding.floatingCreatePerson.setOnClickListener {
            val i = Intent(this, ClientActivity::class.java)
            startActivity(i)
        }

        val adapter = ListAdapter()
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)

    }
}