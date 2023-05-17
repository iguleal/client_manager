package com.example.clientmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.clientmanager.databinding.ActivityClientBinding
import com.example.clientmanager.databinding.ActivityListBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client

class ClientActivity : AppCompatActivity() {

    lateinit var binding: ActivityClientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnPayment.setOnClickListener {
            val name = binding.editName.text.toString()
            val mobile= binding.editMobile.text.toString()
            val contact= binding.editContact.text.toString()
            val desc= binding.editIssueDesc.text.toString()

            Thread {
                val app = application as App
                val dao = app.db.clientDao()
                val client = Client(
                    name=name,
                    mobile = mobile,
                    contact = contact,
                    desc = desc
                )
                dao.insert(client)
                runOnUiThread {
                    val i = Intent(this, PaymentActivity::class.java)
                    i.putExtra("clientId", client.id)
                    startActivity(i)
                }
            }.start()

        }

        binding.btnFinish.setOnClickListener {

        }
    }
}