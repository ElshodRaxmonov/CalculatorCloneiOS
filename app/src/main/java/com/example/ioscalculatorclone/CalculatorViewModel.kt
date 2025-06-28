package com.example.ioscalculatorclone


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.objecthunter.exp4j.ExpressionBuilder


class CalculatorViewModel : ViewModel() {

    private val _display = MutableLiveData("0")
    val display: LiveData<String> = _display as LiveData<String>
    private val _operation = MutableLiveData("")
    val operation: LiveData<String> = _operation as LiveData<String>

    var currentInput = "0"
    var lastResult = ""
    var lastOperation = ""


    fun onNumberClick(value: String) {
        if (lastResult.isNotEmpty() && lastOperation.isNotEmpty()) {
            currentInput = ""
            lastOperation = ""
            _operation.value = ""

        }
        if (lastResult == "0" || currentInput == "0" || lastResult == "Undefined") {
            currentInput = ""
            lastResult = ""
            lastOperation = ""
            _operation.value = lastOperation
        }

        if (value == "." && currentInput.contains(".")) return
        if (value == "." && currentInput == "") {
            currentInput += "0"
        }


        currentInput += value
        if (value == ".") {
            currentInput = currentInput
                .replace(Regex("(?<=[\\-+*/])\\."), "0.")
        }
        _display.value = currentInput
        Log.d("TAG", "calculate: $currentInput")
    }

    fun onOperatorClick(op: String) {


        if (lastOperation == "minusNumber") lastOperation = ""
        if (currentInput == "0" && op == "-") {
            currentInput = op
            lastOperation = "minusNumber"
            _display.value = currentInput
        }
        if (lastOperation.isNotEmpty() && lastResult.isNotEmpty()) {
            currentInput = lastResult
            lastResult = ""
            currentInput += op
            _display.value = currentInput

        }
        if (currentInput.matches(Regex(".*[/*].$"))) {
            currentInput = currentInput.dropLast(2)
        }
        when (currentInput.last()) {

            '/', '*' -> if (op[0] != '-') {
                currentInput = replaceIfOperatorAtEnd(currentInput, op[0])
                _display.value = currentInput
            } else {
                currentInput += op
                _display.value = currentInput
            }

            '-' -> {

                if (op[0] == '-') {
                    _display.value = currentInput
                } else {
                    currentInput = replaceIfOperatorAtEnd(currentInput, op[0])
                    _display.value = currentInput
                }
            }

            '+' -> {
                if (currentInput.matches(Regex(".*[*/]-$"))) {
                    currentInput.dropLast(2)

                } else {
                    currentInput = replaceIfOperatorAtEnd(currentInput, op[0])
                    _display.value = currentInput
                }
            }

            '%' -> {
                currentInput = replaceIfOperatorAtEnd(currentInput, op[0])
                _display.value = currentInput
            }

            else -> {
                currentInput += op
                _display.value = currentInput
            }
        }


    }

    fun onEqualsClick() {
        Log.d("TAG", "calculate: $currentInput")
        if (lastOperation != "minusNumber") {

            currentInput = currentInput.replace(Regex("\\.(?=[\\-+*/])"), "").removeSuffix(".")
            lastOperation = currentInput
            lastResult = calculate(currentInput)
            _display.value = lastResult
            currentInput = ""
        } else {

            _display.value = currentInput
        }


    }

    fun onClearClick() {
        lastOperation = ""
        lastResult = ""
        currentInput = "0"
        _display.value = "0"
        _operation.value = ""
    }


    fun calculate(expression: String): String {
        var calcResult: String = ""
        try {


            var expr = ExpressionBuilder(expression).build().evaluate()
            Log.d("TAG", "calculate: $expr")

            calcResult = when {
                expr.isNaN() || expr.isInfinite() -> "Undefined"
                expr % 1 == 0.0 -> expr.toInt().toString()
                expr % 1 > 0.0 -> String.format("%.3f", expr)
                else -> expr.toString()
            }
        } catch (e: ArithmeticException) {
            calcResult = "Undefined"

        } catch (e: Exception) {
            calcResult = "Invalid"
        }
        return calcResult
    }

    fun replaceIfOperatorAtEnd(input: String, replacement: Char): String {
        return if (input.isNotEmpty() && input.last() in listOf('+', '-', '*', '/', '%')) {
            input.dropLast(1) + replacement
        } else {
            input
        }
    }


}