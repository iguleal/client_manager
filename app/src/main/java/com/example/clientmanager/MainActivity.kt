package com.example.clientmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvMain: RecyclerView = findViewById(R.id.rv_main)

        val adapter = MainAdapter()
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)
    }

 private inner class MainAdapter(): RecyclerView.Adapter<MainAdapter.MainViewHolder>(){
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
         val view = layoutInflater.inflate(R.layout.main_item, parent, false)
         return MainViewHolder(view)
     }

     override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {

     }

     override fun getItemCount(): Int {
         return 5
     }

     private inner class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

     }

 }
}