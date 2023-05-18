package com.example.clientmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientmanager.databinding.ActivityListBinding
import com.example.clientmanager.databinding.ActivityMainBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client

class ListActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding: ActivityListBinding
    var listClient = mutableListOf<Client>()
    lateinit var adapter: ListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rvList: RecyclerView = findViewById(R.id.rv_list)

        binding.floatingCreatePerson.setOnClickListener {
            val i = Intent(this, ClientActivity::class.java)
            startActivity(i)
        }

        queryClient()

        adapter = ListAdapter(listClient, this)
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(id: Int) {
        val i = Intent(this, ClientActivity::class.java)
        i.putExtra("clientId", id)
        startActivity(i)
    }

    override fun onLongClickListener() {

    }

    private fun queryClient() {
        Thread{
            val app = application as App
            val dao = app.db.clientDao()
            val response = dao.getClients()

            runOnUiThread {
                listClient.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()
    }
}