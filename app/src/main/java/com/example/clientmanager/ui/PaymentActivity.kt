package com.example.clientmanager.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.clientmanager.Constants
import com.example.clientmanager.R
import com.example.clientmanager.databinding.ActivityPaymentBinding
import com.example.clientmanager.model.App

class PaymentActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_LIST = "list"
        const val RESULT = 100
    }

    private lateinit var binding : ActivityPaymentBinding
    private var clientId: Int = Constants.CLIENT.NEW_CLIENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = resources.getStringArray(R.array.auto_card)
        binding.autoCard.setText(items.first())

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        binding.autoCard.setAdapter(adapter)

        binding.autoCard.setOnClickListener {
            val keyboardService =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboardService.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        clientId = intent?.extras?.getInt("clientId") ?: Constants.CLIENT.NEW_CLIENT

        onClickClient(clientId)

        binding.btnFinish.setOnClickListener {

            val totalValue = binding.editTotalValue.text.toString()
            val cash = binding.editCashValue.text.toString()
            val pix = binding.editPixValue.text.toString()
            val card = binding.editCardValue.text.toString()
            val cardTimes = binding.autoCard.text.toString()


            val list = arrayListOf(totalValue, cash, pix, card, cardTimes)

            val paymentList = validate(list)

            setResult(
                RESULT,
                Intent().putExtra(EXTRA_LIST, paymentList)
            )
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun onClickClient(clientId: Int) {
        if (clientId == Constants.CLIENT.NEW_CLIENT){
            return
        }

        Thread{
            val app = application as App
            val dao = app.db.clientDao()
            val client = dao.getClientById(clientId)

            runOnUiThread {
                binding.editTotalValue.setText(client.totalValue.toString())
                binding.editCashValue.setText(client.cashValue.toString())
                binding.editPixValue.setText(client.pixValue.toString())
                binding.editCardValue.setText(client.cardValue.toString())
                binding.autoCard.setText(client.cardTimes.toString())
            }
        }.start()
    }

    private fun validate(list: List<String>): ArrayList<Int> {

        val listInt = arrayListOf<Int>()

        list.forEach {
            if (it == "") {
                listInt.add(0)
            } else {
                listInt.add(it.toInt())
            }
        }

        val totalValue = listInt[0]
        val cash = listInt[1]
        val pix = listInt[2]
        val card = listInt[3]
        val times = listInt[4]

        if (totalValue - (cash + pix + (card * times)) == 0) {
            listInt.add(Constants.CLIENT.PAID_TRUE)
        } else {
            listInt.add(Constants.CLIENT.PAID_FALSE)
        }

        return listInt
    }
}


