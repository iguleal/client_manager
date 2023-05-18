package com.example.clientmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.clientmanager.databinding.ActivityClientBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client

class ClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientBinding
    private lateinit var paymentList: ArrayList<Int>
    private var clientId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clientId = intent?.extras?.getInt("clientId") ?: -1

        onClickClient(clientId)

        val launcherData = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == PaymentActivity.RESULT){

                    paymentList = result.data?.getIntegerArrayListExtra(PaymentActivity.EXTRA_LIST)!!

                if (paymentList[4] == 1) {
                    binding.imgPayment.setImageResource(R.drawable.ic_paid)
                }

            }
        }

        binding.btnPayment.setOnClickListener {

            if (clientId == -1) {
                val i = Intent(this@ClientActivity, PaymentActivity::class.java)
                launcherData.launch(i)
            } else {
                val i = Intent(this, PaymentActivity::class.java)
                i.putExtra("clientId", clientId)
                startActivity(i)
            }
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

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun onClickClient(clientId:Int) {
        if (clientId == -1){
            return
        }

        Thread{
            val app = application as App
            val dao = app.db.clientDao()
            val client = dao.getClientById(clientId)

            runOnUiThread {
                binding.editName.hint = client.name
                binding.editName.setHintTextColor(ContextCompat.getColor(this,R.color.black))
                binding.editMobile.hint = client.mobile
                binding.editMobile.setHintTextColor(ContextCompat.getColor(this,R.color.black))
                binding.editContact.hint = client.contact
                binding.editContact.setHintTextColor(ContextCompat.getColor(this,R.color.black))
                binding.editIssueDesc.hint = client.desc
                binding.editIssueDesc.setHintTextColor(ContextCompat.getColor(this,R.color.black))
                binding.radioDone.isChecked = client.isFixed
                if (client.isPaid ) binding.imgPayment.setImageResource(R.drawable.ic_paid)

            }
        }.start()
    }
}