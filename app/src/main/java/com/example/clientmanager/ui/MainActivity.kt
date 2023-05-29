package com.example.clientmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientmanager.Constants
import com.example.clientmanager.R
import com.example.clientmanager.databinding.ActivityMainBinding
import com.example.clientmanager.model.MainItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clClient.setOnClickListener {
            val i = Intent(this@MainActivity, ListActivity::class.java)
            i.putExtra("title", Constants.TITLE.CLIENTS)
            startActivity(i)
        }

        val rvMain: RecyclerView = findViewById(R.id.rv_main)

        val listItem = mutableListOf<MainItem>()
        listItemAdd(listItem)
        val adapter = MainAdapter(listItem) { title ->
            when (title) {
                Constants.TITLE.SERVICES -> {
                    val i = Intent(this@MainActivity, ListActivity::class.java)
                    i.putExtra("title", title)
                    startActivity(i)
                }

                Constants.TITLE.FIXED -> {
                    val i = Intent(this@MainActivity, ListActivity::class.java)
                    i.putExtra("title", title)
                    startActivity(i)
                }

                Constants.TITLE.NOT_PAID -> {
                    val i = Intent(this@MainActivity, ListActivity::class.java)
                    i.putExtra("title", title)
                    startActivity(i)
                }

                Constants.TITLE.PAID -> {
                    val i = Intent(this@MainActivity, ListActivity::class.java)
                    i.putExtra("title", title)
                    startActivity(i)
                }
            }
        }
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)
    }

    private fun listItemAdd(list: MutableList<MainItem>) {
        list.add(
            MainItem(
                id = 1, title = Constants.TITLE.SERVICES, img = R.drawable.ic_services
            )
        )
        list.add(
            MainItem(
                id = 2, title = Constants.TITLE.FIXED, img = R.drawable.ic_resolved
            )
        )
        list.add(
            MainItem(
                id = 3, title = Constants.TITLE.NOT_PAID, img = R.drawable.ic_payoff
            )
        )
        list.add(
            MainItem(
                id = 4, title = Constants.TITLE.PAID, img = R.drawable.ic_billed
            )
        )
    }

    private inner class MainAdapter(
        val list: MutableList<MainItem>,
        val listener: (String) -> Unit
    ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = list[position]
            holder.bind(itemCurrent)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(itemCurrent: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.iv_icon_img)
                val name: TextView = itemView.findViewById(R.id.txt_icon_name)
                val container: ConstraintLayout = itemView.findViewById(R.id.container)

                img.setImageResource(itemCurrent.img)
                name.text = itemCurrent.title

                container.setOnClickListener {
                    listener.invoke(itemCurrent.title)
                }
            }
        }
    }
}