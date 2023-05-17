package com.example.clientmanager

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.clientmanager.databinding.ActivityPaymentBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client

class PaymentActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val autoCard: AutoCompleteTextView = findViewById(R.id.auto_card)

        val items = resources.getStringArray(R.array.auto_card)
        autoCard.setText(items.first())

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        autoCard.setAdapter(adapter)

        autoCard.setOnClickListener {
            val keyboardService =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboardService.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        binding.btnFinish.setOnClickListener {
            registerClient()
        }
    }

    private fun registerClient() {
        val clientId = intent?.extras?.getInt("clientId")
            ?: throw IllegalAccessException("ClientId not found")
        val totalValue = binding.editTotalValue.text.toString().toInt()
        val cash = binding.editCashValue.text.toString().toInt()
        val pix = binding.editPixValue.text.toString().toInt()
        val card = binding.editCardValue.text.toString().toInt()
//        val cardTimes = binding.autoCard.text.toString().toInt()

        Thread {
            val app = application as App
            val dao = app.db.clientDao()
            val client = dao.getClientById(clientId)
            dao.update(Client(
                id = client.id,
                name = client.name,
                mobile = client.mobile,
                contact = client.contact,
                desc = client.desc,
                isFixed = true,
                totalValue=totalValue,
                cashValue = cash,
                pixValue = pix,
                cardValue = card,
                cardTimes = 2
            ))

            runOnUiThread{
//                finish()
            }
        }.start()
    }
}