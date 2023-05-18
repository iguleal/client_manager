package com.example.clientmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.clientmanager.databinding.ActivityClientBinding
import com.example.clientmanager.databinding.ActivityListBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client
import java.util.ArrayList

class ClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientBinding
    private lateinit var paymentList: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        paymentList = (intent?.extras?.getIntArray("list") ?: throw IllegalStateException("type not found")) as Array<Int>

        val launcherData = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == PaymentActivity.RESULT){

                    paymentList = result.data?.getIntegerArrayListExtra(PaymentActivity.EXTRA_LIST)!!

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
                )

                dao.insert(client)

                runOnUiThread {
                    Toast.makeText(this,"foi",Toast.LENGTH_SHORT).show()
                }

            }.start()
        }
    }
}