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

        // Initialize buttons
        initializeButtons(inputTextView, resultTextView)
    }

    private fun initializeButtons(inputTextView: TextView, resultTextView: TextView) {
        val buttonClear = findViewById<Button>(R.id.buttonClear)
        val buttonEquals = findViewById<Button>(R.id.buttonEquals)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        val buttonPeriod = findViewById<Button>(R.id.buttonPeriod)

        // Operation buttons
        setupOperationButtons(inputTextView)

        // Number buttons
        setupNumberButtons(inputTextView)

        // Handle decimal point
        buttonPeriod.setOnClickListener { handleDecimalPoint(inputTextView) }

        // Equals button
        buttonEquals.setOnClickListener { calculateResult(resultTextView) }

        // Clear button
        buttonClear.setOnClickListener { clearInputs(inputTextView, resultTextView) }

        // Delete button
        buttonDelete.setOnClickListener { deleteInput(inputTextView) }
    }

    private fun setupNumberButtons(inputTextView: TextView) {
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

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                handleNumberInput(index.toString(), inputTextView)
            }
        }
    }

    private fun handleNumberInput(digit: String, inputTextView: TextView) {
        if (operation == null) {
            addDigitToFirstNumber(digit, inputTextView)
        } else {
            addDigitToSecondNumber(digit, inputTextView)
        }
    }

    private fun addDigitToFirstNumber(digit: String, inputTextView: TextView) {
        if (canAddDigit(firstNumberString)) {
            firstNumberString += digit
            inputTextView.text = firstNumberString
        }
    }

    private fun addDigitToSecondNumber(digit: String, inputTextView: TextView) {
        if (canAddDigit(secondNumberString)) {
            secondNumberString += digit
            inputTextView.text = "$firstNumberString $operation $secondNumberString"
        }
    }

    private fun canAddDigit(currentString: String): Boolean {
        return currentString.length < 2 || (currentString.contains(".") && currentString.split(".")[1].length < 2)
    }

    private fun handleDecimalPoint(inputTextView: TextView) {
        if (operation == null) {
            addDecimalPointToFirstNumber(inputTextView)
        } else {
            addDecimalPointToSecondNumber(inputTextView)
        }
    }

    private fun addDecimalPointToFirstNumber(inputTextView: TextView) {
        if (!firstNumberString.contains(".")) {
            firstNumberString = if (firstNumberString.isEmpty()) "0." else "${firstNumberString}."
            inputTextView.text = firstNumberString
        }
    }

    private fun addDecimalPointToSecondNumber(inputTextView: TextView) {
        if (!secondNumberString.contains(".")) {
            secondNumberString = if (secondNumberString.isEmpty()) "0." else "${secondNumberString}."
            inputTextView.text = "$firstNumberString $operation $secondNumberString"
        }
    }

    private fun setupOperationButtons(inputTextView: TextView) {
        findViewById<Button>(R.id.buttonAdd).setOnClickListener { setOperation("+", inputTextView) }
        findViewById<Button>(R.id.buttonSubtract).setOnClickListener { setOperation("-", inputTextView) }
        findViewById<Button>(R.id.buttonMultiply).setOnClickListener { setOperation("×", inputTextView) }
        findViewById<Button>(R.id.buttonDivide).setOnClickListener { setOperation("÷", inputTextView) }
    }

    private fun setOperation(op: String, inputTextView: TextView) {
        if (firstNumberString.isNotEmpty() && secondNumberString.isEmpty()) {
            operation = op
            inputTextView.text = "$firstNumberString $op"
        }
    }

    private fun calculateResult(resultTextView: TextView) {
        if (firstNumberString.isNotEmpty() && secondNumberString.isNotEmpty() && operation != null) {
            val firstNumber = firstNumberString.toDouble()
            val secondNumber = secondNumberString.toDouble()

            val result = when (operation) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "×" -> firstNumber * secondNumber
                "÷" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN
                else -> 0.0
            }
            resultTextView.text = decimalFormat.format(result)
        }
    }

    private fun clearInputs(inputTextView: TextView, resultTextView: TextView) {
        firstNumberString = ""
        secondNumberString = ""
        operation = null
        inputTextView.text = "0"
        resultTextView.text = "0.00"
    }

    private fun deleteInput(inputTextView: TextView) {
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
