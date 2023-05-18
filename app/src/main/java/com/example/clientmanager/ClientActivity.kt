package com.example.clientmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.clientmanager.databinding.ActivityClientBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client

class ClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientBinding
    private lateinit var paymentList: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val launcherData = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == PaymentActivity.RESULT){

                    paymentList = result.data?.getIntegerArrayListExtra(PaymentActivity.EXTRA_LIST)!!

                if (paymentList[4] == 1) {
                    binding.imgPayment.setImageResource(R.drawable.ic_paid)
                }

                Log.i("teste", paymentList.toString())
            }
        }

        binding.btnPayment.setOnClickListener {

            val i = Intent(this@ClientActivity, PaymentActivity::class.java)
            launcherData.launch(i)
        }

        binding.btnSave.setOnClickListener {

            val name = binding.editName.text.toString()
            val mobile= binding.editMobile.text.toString()
            val contact= binding.editContact.text.toString()
            val desc= binding.editIssueDesc.text.toString()

            Thread {
                val app = application as App
                val dao = app.db.clientDao()
                val client = Client(
                    name = name,
                    mobile = mobile,
                    contact = contact,
                    desc = desc,
                    totalValue= paymentList[0],
                    cashValue = paymentList[1],
                    pixValue = paymentList[2],
                    cardValue = paymentList[3],
                    isPaid = paymentList[4] == 1
                )

                dao.insert(client)

                runOnUiThread {
                    finish()
                }

            }.start()
        }
    }
}