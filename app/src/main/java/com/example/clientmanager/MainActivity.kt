package com.example.clientmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientmanager.databinding.ActivityMainBinding
import com.example.clientmanager.databinding.ActivityPaymentBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvMain: RecyclerView = findViewById(R.id.rv_main)

        val listItem = mutableListOf<MainItem>()
        listItemAdd(listItem)

        val adapter = MainAdapter(listItem) { id ->
            when (id) {
                1 -> startActivity(Intent(this@MainActivity, ListActivity::class.java))
//                2 ->
            }
        }
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)

    }

    private fun listItemAdd(list: MutableList<MainItem>) {
        list.add(
            MainItem(
                id = 1, title = "CLIENTES", img = R.drawable.ic_clients
            )
        )
        list.add(
            MainItem(
                id = 2, title = "SERVIÇOS", img = R.drawable.ic_services
            )
        )
        list.add(
            MainItem(
                id = 3, title = "RESOLVIDO", img = R.drawable.ic_resolved
            )
        )
        list.add(
            MainItem(
                id = 4, title = "À PAGAR", img = R.drawable.ic_payoff
            )
        )
        list.add(
            MainItem(
                id = 5, title = "FATURADO", img = R.drawable.ic_billed
            )
        )
    }

    private inner class MainAdapter(
        val list: MutableList<MainItem>,
        val listener: (Int) -> Unit
    ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MainAdapter.MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {
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
                    listener.invoke(itemCurrent.id)
                }
            }
        }
    }
}