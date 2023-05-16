package com.example.clientmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        val btnPayment: Button = findViewById(R.id.btn_payment)

        btnPayment.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }

        val btnConclude: Button = findViewById(R.id.btn_conclude)
        btnConclude.setOnClickListener {
            finish()
        }
    }
}