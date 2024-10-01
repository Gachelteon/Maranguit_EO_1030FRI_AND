package com.maranguit.bottomnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.DecimalFormat

class Calculator : Fragment() {

    private var firstNumberString: String = ""
    private var secondNumberString: String = ""
    private var operation: String? = null
    private val decimalFormat = DecimalFormat("#.00")

    // Views
    private lateinit var inputTextView: TextView
    private lateinit var resultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)

        // Initialize views
        initializeViews(view)

        // Initialize buttons
        initializeButtons(view)

        return view
    }

    private fun initializeViews(view: View) {
        inputTextView = view.findViewById(R.id.inputText)
        resultTextView = view.findViewById(R.id.resultText)
    }

    private fun initializeButtons(view: View) {
        // Operation buttons
        view.findViewById<Button>(R.id.buttonAdd).setOnClickListener { setOperation("+") }
        view.findViewById<Button>(R.id.buttonSubtract).setOnClickListener { setOperation("-") }
        view.findViewById<Button>(R.id.buttonMultiply).setOnClickListener { setOperation("×") }
        view.findViewById<Button>(R.id.buttonDivide).setOnClickListener { setOperation("÷") }

        // Equals and Clear
        view.findViewById<Button>(R.id.buttonEquals).setOnClickListener { calculateResult() }
        view.findViewById<Button>(R.id.buttonClear).setOnClickListener { clearInputs() }

        // Delete and Period
        view.findViewById<Button>(R.id.buttonDelete).setOnClickListener { deleteInput() }
        view.findViewById<Button>(R.id.buttonPeriod).setOnClickListener { addPeriod() }

        // Number buttons
        val numberButtons = listOf(
            view.findViewById<Button>(R.id.button0),
            view.findViewById<Button>(R.id.button1),
            view.findViewById<Button>(R.id.button2),
            view.findViewById<Button>(R.id.button3),
            view.findViewById<Button>(R.id.button4),
            view.findViewById<Button>(R.id.button5),
            view.findViewById<Button>(R.id.button6),
            view.findViewById<Button>(R.id.button7),
            view.findViewById<Button>(R.id.button8),
            view.findViewById<Button>(R.id.button9)
        )
        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener { handleNumberInput(index.toString()) }
        }
    }

    private fun handleNumberInput(digit: String) {
        if (operation == null) {
            firstNumberString = addDigit(firstNumberString, digit)
            updateInputText(firstNumberString)
        } else {
            secondNumberString = addDigit(secondNumberString, digit)
            updateInputText("$firstNumberString $operation $secondNumberString")
        }
    }

    private fun setOperation(op: String) {
        if (firstNumberString.isNotEmpty() && secondNumberString.isEmpty()) {
            operation = op
            updateInputText("$firstNumberString $op")
        }
    }

    private fun addPeriod() {
        if (operation == null && !firstNumberString.contains(".")) {
            if (firstNumberString.isEmpty()) firstNumberString = "0"
            firstNumberString += "."
            updateInputText(firstNumberString)
        } else if (operation != null && !secondNumberString.contains(".")) {
            if (secondNumberString.isEmpty()) secondNumberString = "0"
            secondNumberString += "."
            updateInputText("$firstNumberString $operation $secondNumberString")
        }
    }

    private fun calculateResult() {
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

    private fun clearInputs() {
        firstNumberString = ""
        secondNumberString = ""
        operation = null
        inputTextView.text = "0"
        resultTextView.text = "0.00"
    }

    private fun deleteInput() {
        when {
            secondNumberString.isNotEmpty() -> {
                secondNumberString = secondNumberString.dropLast(1)
                updateInputText("$firstNumberString $operation $secondNumberString")
            }
            operation != null -> {
                operation = null
                updateInputText(firstNumberString)
            }
            firstNumberString.isNotEmpty() -> {
                firstNumberString = firstNumberString.dropLast(1)
                updateInputText(firstNumberString.ifEmpty { "0" })
            }
        }
    }

    private fun updateInputText(text: String) {
        inputTextView.text = text
    }

    private fun addDigit(currentString: String, digit: String): String {
        val parts = currentString.split(".")

        return when {
            parts.size == 2 && parts[1].length >= 2 -> currentString // Limit to 2 decimal places
            parts.size == 1 && parts[0].length >= 2 -> currentString // Limit to 2 digits for whole part
            currentString.length < 10 -> currentString + digit // Allow more digits until the limit is reached
            else -> currentString // If limits are reached, do nothing
        }
    }
}
