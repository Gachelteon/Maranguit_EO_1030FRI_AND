package com.maranguit.simplecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private var firstNumberString: String = ""
    private var secondNumberString: String = ""
    private var operation: String? = null
    private val decimalFormat = DecimalFormat("#.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Initialize views
        val inputTextView = findViewById<TextView>(R.id.inputText)
        val resultTextView = findViewById<TextView>(R.id.resultText)

        val buttonClear = findViewById<Button>(R.id.buttonClear)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val buttonSubtract = findViewById<Button>(R.id.buttonSubtract)
        val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
        val buttonDivide = findViewById<Button>(R.id.buttonDivide)
        val buttonEquals = findViewById<Button>(R.id.buttonEquals)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        val buttonPeriod = findViewById<Button>(R.id.buttonPeriod)

        // Number buttons
        val numberButtons = listOf(
            findViewById<Button>(R.id.button0),
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button2),
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6),
            findViewById<Button>(R.id.button7),
            findViewById<Button>(R.id.button8),
            findViewById<Button>(R.id.button9)
        )

        // Handle number inputs
        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (operation == null) {
                    // Handle first number
                    if (firstNumberString.length < 2 || (firstNumberString.contains(".") && firstNumberString.split(".")[1].length < 2)) {
                        firstNumberString += index.toString()
                        inputTextView.text = firstNumberString
                    }
                } else {
                    // Handle second number
                    if (secondNumberString.length < 2 || (secondNumberString.contains(".") && secondNumberString.split(".")[1].length < 2)) {
                        secondNumberString += index.toString()
                        inputTextView.text = "${firstNumberString} $operation $secondNumberString"
                    }
                }
            }
        }


        // Handle decimal point
        buttonPeriod.setOnClickListener {
            if (operation == null && !firstNumberString.contains(".")) {
                if (firstNumberString.isEmpty()) firstNumberString = "0"
                firstNumberString += "."
                inputTextView.text = firstNumberString
            } else if (operation != null && !secondNumberString.contains(".")) {
                if (secondNumberString.isEmpty()) secondNumberString = "0"
                secondNumberString += "."
                inputTextView.text = "${firstNumberString} $operation $secondNumberString"
            }
        }

        // Handle operations
        buttonAdd.setOnClickListener { setOperation("+", inputTextView) }
        buttonSubtract.setOnClickListener { setOperation("-", inputTextView) }
        buttonMultiply.setOnClickListener { setOperation("×", inputTextView) }
        buttonDivide.setOnClickListener { setOperation("÷", inputTextView) }

        // Equals button to calculate the result
        buttonEquals.setOnClickListener {
            if (firstNumberString.isNotEmpty() && secondNumberString.isNotEmpty() && operation != null) {
                val firstNumber = firstNumberString.toDouble()
                val secondNumber = secondNumberString.toDouble()

                val result = when (operation) {
                    "+" -> firstNumber + secondNumber
                    "-" -> firstNumber - secondNumber
                    "×" -> firstNumber * secondNumber
                    "÷" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN // Handle division by zero
                    else -> 0.0
                }
                resultTextView.text = decimalFormat.format(result)
            }
        }

        // Clear button to reset inputs
        buttonClear.setOnClickListener {
            firstNumberString = ""
            secondNumberString = ""
            operation = null
            inputTextView.text = "0"
            resultTextView.text = "0.00"
        }

        // Delete button functionality
        buttonDelete.setOnClickListener {
            when {
                secondNumberString.isNotEmpty() -> {
                    secondNumberString = secondNumberString.dropLast(1)
                    inputTextView.text = "${firstNumberString} $operation $secondNumberString"
                }
                operation != null -> {
                    operation = null
                    inputTextView.text = firstNumberString
                }
                firstNumberString.isNotEmpty() -> {
                    firstNumberString = firstNumberString.dropLast(1)
                    inputTextView.text = firstNumberString.ifEmpty { "0" }
                }
            }
        }
    }



    private fun setOperation(op: String, inputTextView: TextView) {
        if (firstNumberString.isNotEmpty() && secondNumberString.isEmpty()) {
            operation = op
            inputTextView.text = "$firstNumberString $op"
        }
    }

    private fun addDigit(currentString: String, digit: String): String {
        val parts = currentString.split(".")
        return when {
            parts.size == 2 && parts[1].length >= 2 -> currentString // Limit decimal places to 2
            currentString.length < 10 -> currentString + digit // Limit total length
            else -> currentString
        }
    }
}