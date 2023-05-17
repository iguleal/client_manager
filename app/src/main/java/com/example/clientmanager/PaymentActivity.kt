package com.example.clientmanager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

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

        val btnFinish: Button = findViewById(R.id.btn_cancel)
        btnFinish.setOnClickListener {
            finish()
        }
    }
}