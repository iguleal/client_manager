package com.example.clientmanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clientmanager.R
import com.example.clientmanager.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

            val user = binding.editUser.text.toString()
            val password = binding.editPassword.text.toString()

            if ( user != "admin" || password != "admin") {
                Toast.makeText(this@LoginActivity, R.string.invalid_login, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}