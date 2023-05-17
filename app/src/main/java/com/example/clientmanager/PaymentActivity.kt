package com.example.clientmanager

import android.content.Context
import android.content.Intent
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

    companion object{
        const val EXTRA = "list"
        const val RESULT = 100
    }

    private lateinit var binding : ActivityPaymentBinding
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

        binding.btnFinish.setOnClickListener {
//            val totalValue = binding.editTotalValue.text.toString().toInt()
//            val cash = binding.editCashValue.text.toString().toInt()
//            val pix = binding.editPixValue.text.toString().toInt()
//            val card = binding.editCardValue.text.toString().toInt()

//            val paymentList = arrayOf(totalValue,cash, pix, card)
            val paymentList = "olá"

            setResult(
                RESULT,
                Intent().putExtra(EXTRA, paymentList)
            )
            finish()
        }
    }
}