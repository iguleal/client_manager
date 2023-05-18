package com.example.clientmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.clientmanager.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_LIST = "list"
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

            val totalValue = binding.editTotalValue.text.toString()
            val cash = binding.editCashValue.text.toString()
            val pix = binding.editPixValue.text.toString()
            val card = binding.editCardValue.text.toString()

            val list = arrayListOf(totalValue, cash, pix, card)

            val paymentList = validate(list)

            setResult(
                RESULT,
                Intent().putExtra(EXTRA_LIST, paymentList)
            )
            finish()
        }
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

        // 200 - 0 - 150 - 50

        val a = listInt[0]
        val b = listInt[1]
        val c = listInt[2]
        val d = listInt[3]

        val calculate = if (a - (b + c + d) == 0) {
            listInt.add(1)
        } else {
            listInt.add(0)
        }

        return listInt
    }
}