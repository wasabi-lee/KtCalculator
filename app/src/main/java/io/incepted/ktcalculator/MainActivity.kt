package io.incepted.ktcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {

    // Non nullable variable but will initialize later. Only compatible with var.
//    private lateinit var result: EditText
//    private lateinit var newNumber: EditText
//
//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) {
//        // Executes this function whenever this field is referenced for the first time
//        // and the value is cached when the value is accessed again
//        // Thread safe if using only 'by lazy' alone.
//        // Setting the 'NONE' flag because this widget and the activity are going to be accessed from main thread only.
//        findViewById<TextView>(R.id.main_operation)
//    }

    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { v ->
            val button = v as Button
            main_new_number_edit.append(button.text)
        }

        main_btn_zero.setOnClickListener(listener)
        main_btn_one.setOnClickListener(listener)
        main_btn_two.setOnClickListener(listener)
        main_btn_three.setOnClickListener(listener)
        main_btn_four.setOnClickListener(listener)
        main_btn_five.setOnClickListener(listener)
        main_btn_six.setOnClickListener(listener)
        main_btn_seven.setOnClickListener(listener)
        main_btn_eight.setOnClickListener(listener)
        main_btn_nine.setOnClickListener(listener)
        main_btn_decimal.setOnClickListener(listener)



        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = main_new_number_edit.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                main_new_number_edit.setText("")
            }
            pendingOperation = op
            main_operation.text = pendingOperation
        }

        main_btn_equal.setOnClickListener(opListener)
        main_btn_divide.setOnClickListener(opListener)
        main_btn_multiply.setOnClickListener(opListener)
        main_btn_plus.setOnClickListener(opListener)
        main_btn_subtract.setOnClickListener(opListener)

        main_btn_neg.setOnClickListener {
            val value = main_new_number_edit.text.toString()
            if (value.isEmpty()) {
                main_new_number_edit.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    main_new_number_edit.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    // newNumber was "-" or ".", so clear it
                    main_new_number_edit.setText("")
                }
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {  // Kotlin can use == for dealing with the object!
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN   // handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        main_result_edit.setText(operand1.toString())
        main_new_number_edit.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(STATE_PENDING_OPERATION, pendingOperation)
        if (operand1 != null) {
            outState?.putDouble(STATE_OPERAND1, operand1!!)
            outState?.putBoolean(STATE_OPERAND1_STORED, true)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState?.getString(STATE_PENDING_OPERATION, "=") ?: "="

        val stored: Boolean = savedInstanceState?.getBoolean(STATE_OPERAND1_STORED, false)!!

        operand1 = if (stored) savedInstanceState.getDouble(STATE_OPERAND1) else null

        main_operation.text = pendingOperation
    }
}
