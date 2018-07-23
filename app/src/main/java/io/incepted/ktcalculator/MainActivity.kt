package io.incepted.ktcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // Non nullable variable but will initialize later. Only compatible with var.
    private lateinit var result: EditText
    private lateinit var newNumber: EditText

    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) {
        // Executes this function whenever this field is referenced for the first time
        // and the value is cached when the value is accessed again
        // Thread safe if using only 'by lazy' alone.
        // Setting the 'NONE' flag because this widget and the activity are going to be accessed from main thread only.
        findViewById<TextView>(R.id.main_operation)
    }

    private var operand1: Double? = null
    private var operand2: Double = 0.0
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.main_result_edit)
        newNumber = findViewById(R.id.main_new_number_edit)

        // Data input buttons
        val button0: Button = findViewById(R.id.main_btn_zero)
        val button1: Button = findViewById(R.id.main_btn_one)
        val button2: Button = findViewById(R.id.main_btn_two)
        val button3: Button = findViewById(R.id.main_btn_three)
        val button4: Button = findViewById(R.id.main_btn_four)
        val button5: Button = findViewById(R.id.main_btn_five)
        val button6: Button = findViewById(R.id.main_btn_six)
        val button7: Button = findViewById(R.id.main_btn_seven)
        val button8: Button = findViewById(R.id.main_btn_eight)
        val button9: Button = findViewById(R.id.main_btn_nine)
        val buttonDot: Button = findViewById(R.id.main_btn_decimal)

        // Operation buttons
        val buttonEquals = findViewById<Button>(R.id.main_btn_equal)
        val buttonDivide = findViewById<Button>(R.id.main_btn_divide)
        val buttonMultiply = findViewById<Button>(R.id.main_btn_multiply)
        val buttonPlus = findViewById<Button>(R.id.main_btn_plus)
        val buttonMinus = findViewById<Button>(R.id.main_btn_subtract)

        val listener = View.OnClickListener { v ->
            val button = v as Button
            newNumber.append(button.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            val value = newNumber.text.toString()
            if (value.isNotEmpty()) {
                performOperation(value, op)
            }
            pendingOperation = op
            displayOperation.text = pendingOperation
        }


        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
    }

    private fun performOperation(value: String, operation: String) {
        if (operand1 == null) {
            operand1 = value.toDouble()
        } else {
            operand2 = value.toDouble()
            if (pendingOperation == "=") {  // Kotlin can use == for dealing with the object!
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand1 = operand2
                "/" -> if (operand2 == 0.0) {
                           operand1 = Double.NaN   // handle attempt to divide by zero
                        } else {
                            operand1 = operand1!! / operand2
                }
                "*" -> operand1 = operand1!! * operand2
                "-" -> operand1 = operand1!! - operand2
                "+" -> operand1 = operand1!! + operand2
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }
}
