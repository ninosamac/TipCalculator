package com.example.tip_calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
private const val DEFAULT_NUMBER_OF_PEOPLE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var billAmount: EditText
    private lateinit var buttonAdd: Button
    private lateinit var seekBar: SeekBar
    private lateinit var tipPercent: TextView
    private lateinit var tipAmount: TextView
    private lateinit var totalAmount: TextView
    private lateinit var tipDescription: TextView
    private lateinit var numberOfPeople: EditText
    private lateinit var amountPerPerson: TextView



    private  var sumBills: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        billAmount = findViewById(R.id.billAmount)
        buttonAdd = findViewById(R.id.buttonClear)
        seekBar = findViewById(R.id.seekBar)
        tipPercent = findViewById(R.id.tipPercent)
        tipAmount = findViewById(R.id.tipAmount)
        totalAmount = findViewById(R.id.totalValue)
        tipDescription = findViewById(R.id.tipDecription)
        numberOfPeople = findViewById(R.id.numberOfPeople)
        amountPerPerson = findViewById(R.id.amountPerPerson)


        seekBar.progress = INITIAL_TIP_PERCENT
        tipPercent.text = "$INITIAL_TIP_PERCENT%"
        numberOfPeople.setText("$DEFAULT_NUMBER_OF_PEOPLE")

        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, tipPercent: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $tipPercent")
                this@MainActivity.tipPercent.text = "$tipPercent%"
                updateAmounts()
                updateTipDescription(tipPercent)
                // Update color
                val color = ArgbEvaluator.getInstance().evaluate(
                    tipPercent.toFloat() / seekBar.max,
                    ContextCompat.getColor(baseContext, R.color.color_worst_tip),
                    ContextCompat.getColor(baseContext, R.color.color_best_tip)
                ) as Int

                tipDescription.setTextColor(color)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })


        billAmount.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(var1: CharSequence?, var2: Int, var3: Int, var4: Int) {

            }

            override fun onTextChanged(var1: CharSequence?, var2: Int, var3: Int, var4: Int) {

            }

            override fun afterTextChanged(billAmount: Editable?) {
                Log.i(TAG, "afterTextChanged: $billAmount")
                updateAmounts()
            }
        })


        numberOfPeople.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(numberOfPeople: Editable?) {
                Log.i(TAG, "numberOfPeople: $numberOfPeople")
                updateAmountPerPerson()
            }

        })

        buttonAdd.setOnClickListener{

            Log.i(TAG, "Button clicked, sum = $sumBills")
            sumBills = 0.0
            billAmount.setText("")

        }
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..0 -> "No tip"
            in 1..9 -> "Small tip"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        this.tipDescription.text = tipDescription

    }

    private fun updateAmounts() {
        if (billAmount.text.isEmpty()) {
            totalAmount.text = ""
            tipAmount.text = ""
            amountPerPerson.text = ""
            return
        }

        // 1. Get the value of base and tip percent
        val billAmount = billAmount.text.toString().toDouble()
        val tipPercent = seekBar.progress

        // 2. Calculate the tip and total
        val tipAmount = billAmount * tipPercent / 100
        val totalAmount = billAmount + tipAmount

        // 3. Update UI
        this.tipAmount.text = "%.2f".format(tipAmount)
        this.totalAmount.text = "%.2f".format(totalAmount)

        updateAmountPerPerson()
    }

    private fun updateAmountPerPerson() {

        if (billAmount.text.isEmpty() || numberOfPeople.text.isEmpty()) {
            return
        }

        // 1. Get the total amount and number of persons
        val totalAmount = totalAmount.text.toString().toDouble()
        val numberOfPeople = numberOfPeople.text.toString().toInt()

        // 2. Split the bill
        val amountPerPerson = totalAmount/numberOfPeople

        // 3. Update UI
        this.amountPerPerson.text = "%.2f".format(amountPerPerson)
    }
}