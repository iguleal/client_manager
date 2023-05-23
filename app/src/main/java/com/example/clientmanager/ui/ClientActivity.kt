package com.example.clientmanager.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.clientmanager.Constants
import com.example.clientmanager.ListAdapter
import com.example.clientmanager.R
import com.example.clientmanager.databinding.ActivityClientBinding
import com.example.clientmanager.model.App
import com.example.clientmanager.model.Client
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientBinding
    private var paymentList = arrayListOf<Int>()
    private var clientId: Int = Constants.CLIENT.NEW_CLIENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clientId = intent?.extras?.getInt("clientId") ?: Constants.CLIENT.NEW_CLIENT

        onClickClient(clientId)

        val launcherData = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == PaymentActivity.RESULT) {

                paymentList = result.data?.getIntegerArrayListExtra(PaymentActivity.EXTRA_LIST)!!

                if (paymentList[5] == Constants.CLIENT.PAID_TRUE) {
                    binding.imgPayment.setImageResource(R.drawable.ic_paid)
                } else if (paymentList[5] == Constants.CLIENT.PAID_FALSE) {
                    binding.imgPayment.setImageResource(R.drawable.ic_not_paid)
                }
            }
        }

        binding.btnPayment.setOnClickListener {

            if (clientId == Constants.CLIENT.NEW_CLIENT) {
                val i = Intent(this@ClientActivity, PaymentActivity::class.java)
                launcherData.launch(i)
            } else {
                val i = Intent(this, PaymentActivity::class.java)
                i.putExtra("clientId", clientId)
                launcherData.launch(i)
            }
        }

        binding.btnSave.setOnClickListener {
            if (clientId == Constants.CLIENT.NEW_CLIENT) {
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
                        totalValue = if (paymentList.isNotEmpty()) paymentList[0] else 0,
                        cashValue = if (paymentList.isNotEmpty()) paymentList[1] else 0,
                        pixValue = if (paymentList.isNotEmpty()) paymentList[2] else 0,
                        cardValue = if (paymentList.isNotEmpty()) paymentList[3] else 0,
                        cardTimes = if (paymentList.isNotEmpty()) paymentList[4] else 0,
                        isPaid = if (paymentList.isNotEmpty()) {
                            paymentList[5] == Constants.CLIENT.PAID_TRUE
                        } else {
                            false
                        }
                    )

                    dao.insert(client)

                    runOnUiThread {
                        //avisar pra list activity atualizar a lista
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
                    var cardTimes = dao.getClientById(clientId).cardTimes
                    var isPaid = dao.getClientById(clientId).isPaid

                    if (paymentList.isNotEmpty()) {
                        totalValue = paymentList[0]
                        cash = paymentList[1]
                        pix = paymentList[2]
                        card = paymentList[3]
                        cardTimes = paymentList[4]
                        isPaid = paymentList[5] == Constants.CLIENT.PAID_TRUE
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
                        cardTimes = cardTimes,
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

        binding.checkDone.setOnCheckedChangeListener { it, b ->
            if (clientId == Constants.CLIENT.NEW_CLIENT) {
                return@setOnCheckedChangeListener
            }

            Thread {
                val app = application as App
                val dao = app.db.clientDao()
                val client = dao.getClientById(clientId)

                dao.update(
                    Client(
                        id = client.id,
                        name = client.name,
                        mobile = client.mobile,
                        contact = client.contact,
                        desc = client.desc,
                        dateFinish = Date(),
                        dateStart = client.dateStart,
                        isPaid = client.isPaid,
                        isFixed = client.isFixed,
                        totalValue = client.totalValue,
                        cashValue = client.cashValue,
                        pixValue = client.pixValue,
                        cardValue = client.cardValue,
                        cardTimes = client.cardTimes
                    )
                )
                runOnUiThread {
                    if (it.isChecked) {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                        val date = sdf.format(client.dateFinish)

                        binding.txtDateFinished.text = date
                    } else {
                        binding.txtDateFinished.text = ""
                    }
                }
            }.start()
        }

    }

    override fun onRestart() {
        super.onRestart()

        binding.view.requestFocus()
        hideKeyboard(this)

    }

    private fun onClickClient(clientId: Int) {
        if (clientId == Constants.CLIENT.NEW_CLIENT) {
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