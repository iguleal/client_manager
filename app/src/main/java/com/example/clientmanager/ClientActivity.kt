package com.example.clientmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.clientmanager.databinding.ActivityClientBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client

class ClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientBinding
    private var paymentList = arrayListOf<Int>()
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

            if (result.resultCode == PaymentActivity.RESULT) {

                paymentList = result.data?.getIntegerArrayListExtra(PaymentActivity.EXTRA_LIST)!!

                if (paymentList[4] == 1) {
                    binding.imgPayment.setImageResource(R.drawable.ic_paid)
                } else if (paymentList[4] == 0) {
                    binding.imgPayment.setImageResource(R.drawable.ic_not_paid)
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
                launcherData.launch(i)
            }
        }

        binding.btnSave.setOnClickListener {
            if (clientId == -1) {
                val name = binding.editName.text.toString()
                val mobile = binding.editMobile.text.toString()
                val contact = binding.editContact.text.toString()
                val desc = binding.editIssueDesc.text.toString()

                Thread {
                    val app = application as App
                    val dao = app.db.clientDao()
                    val client = Client(
                        name = name,
                        mobile = mobile,
                        contact = contact,
                        desc = desc,
                        isFixed = binding.checkDone.isChecked,
                        totalValue = paymentList[0],
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
            } else {
                val name = binding.editName.text.toString()
                val mobile = binding.editMobile.text.toString()
                val contact = binding.editContact.text.toString()
                val desc = binding.editIssueDesc.text.toString()

                Thread {
                    val app = application as App
                    val dao = app.db.clientDao()

                    var totalValue = dao.getClientById(clientId).totalValue
                    var cash = dao.getClientById(clientId).cashValue
                    var pix = dao.getClientById(clientId).pixValue
                    var card = dao.getClientById(clientId).cardValue
                    var isPaid = dao.getClientById(clientId).isPaid

                    if (paymentList.isNotEmpty()) {
                        totalValue = paymentList[0]
                        cash = paymentList[1]
                        pix = paymentList[2]
                        card = paymentList[3]
                        isPaid = paymentList[4] == 1
                    }

                    val client = Client(
                        id = clientId,
                        name = name,
                        mobile = mobile,
                        contact = contact,
                        desc = desc,
                        isFixed = binding.checkDone.isChecked,
                        dateStart = dao.getClientById(clientId).dateStart,

                        totalValue = totalValue,
                        cashValue = cash,
                        pixValue = pix,
                        cardValue = card,

                        isPaid = isPaid
                    )

                    dao.update(client)

                    runOnUiThread {
                        finish()
                    }
                }.start()
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()

        binding.view.requestFocus()
        hideKeyboard(this)

    }

    private fun onClickClient(clientId: Int) {
        if (clientId == -1) {
            return
        }

        Thread {
            val app = application as App
            val dao = app.db.clientDao()
            val client = dao.getClientById(clientId)

            runOnUiThread {
                binding.editName.setText(client.name)
                binding.editMobile.setText(client.mobile)
                binding.editContact.setText(client.contact)
                binding.editIssueDesc.setText(client.desc)
                binding.checkDone.isChecked = client.isFixed
                if (client.isPaid) binding.imgPayment.setImageResource(R.drawable.ic_paid)

            }
        }.start()
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}