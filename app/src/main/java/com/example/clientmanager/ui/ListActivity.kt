package com.example.clientmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientmanager.Constants
import com.example.clientmanager.ListAdapter
import com.example.clientmanager.OnClickListener
import com.example.clientmanager.R
import com.example.clientmanager.databinding.ActivityListBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client

class ListActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityListBinding
    private var listClient = mutableListOf<Client>()
    private lateinit var adapter: ListAdapter
    private lateinit var launcherData: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvList: RecyclerView = findViewById(R.id.rv_list)

        val titleMainItem =
            intent?.extras?.getString("title") ?: throw Exception(" title not found ")

        launcherData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if (it.resultCode == ClientActivity.RESULT_OK) {
                when(titleMainItem){
                    Constants.TITLE.CLIENTS -> {
                        listClient.clear()
                        queryAllClient()
                    }
                    Constants.TITLE.SERVICES -> {
                        listClient.clear()
                        queryClientsByFix(false)
                    }
                    Constants.TITLE.FIXED -> {
                        listClient.clear()
                        queryClientsByFix(true)
                    }
                    Constants.TITLE.PAID -> {
                        listClient.clear()
                        queryClientsByPaid(true)
                    }
                    Constants.TITLE.NOT_PAID -> {
                        listClient.clear()
                        queryClientsByPaid(false)
                    }
                }
            }
        }

        binding.floatingCreatePerson.setOnClickListener {
            val i = Intent(this, ClientActivity::class.java)
            launcherData.launch(i)
        }

        actionBar(titleMainItem)

        when (titleMainItem) {
            Constants.TITLE.CLIENTS -> {
                binding.floatingCreatePerson.visibility = View.VISIBLE
                queryAllClient()
            }

            Constants.TITLE.SERVICES -> {
                binding.floatingCreatePerson.visibility = View.GONE
                queryClientsByFix(false)
            }

            Constants.TITLE.FIXED -> {
                binding.floatingCreatePerson.visibility = View.GONE
                queryClientsByFix(true)
            }

            Constants.TITLE.NOT_PAID -> {
                binding.floatingCreatePerson.visibility = View.GONE
                queryClientsByPaid(false)
            }

            Constants.TITLE.PAID -> {
                binding.floatingCreatePerson.visibility = View.GONE
                queryClientsByPaid(true)
            }
        }

        adapter = ListAdapter(listClient, this)
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(id: Int) {
        val i = Intent(this, ClientActivity::class.java)
        i.putExtra("clientId", id)
        launcherData.launch(i)
    }

    override fun onLongClickListener(client: Client, position: Int) {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete_message)
            .setNegativeButton(R.string.cancel) { _, _ ->
            }
            .setNeutralButton(R.string.delete) { _, _ ->
                Thread{
                    val app = application as App
                    val dao = app.db.clientDao()
                    dao.delete(client)
                    runOnUiThread {
                        listClient.removeAt(position)
                        adapter.notifyItemRemoved(position)
                    }
                }.start()
            }
            .create()
            .show()
    }

    private fun actionBar(titleMainItem: String) {
        setSupportActionBar(binding.toolbarList)
        supportActionBar?.title = titleMainItem
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun queryAllClient() {
        Thread {
            val app = application as App
            val dao = app.db.clientDao()
            val response = dao.getClients()

            runOnUiThread {
                listClient.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()
    }

    private fun queryClientsByFix(isFixed: Boolean) {
        Thread {
            val app = application as App
            val dao = app.db.clientDao()
            val response = dao.getClientByFix(isFixed)

            runOnUiThread {
                listClient.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()
    }

    private fun queryClientsByPaid(isPaid: Boolean) {
        Thread {
            val app = application as App
            val dao = app.db.clientDao()
            val response = dao.getClientByPaid(isPaid)

            runOnUiThread {
                listClient.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()
    }
}