package com.example.basiccalculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.IdRes
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private lateinit var calcDisplay: EditText
    private var x: Double = 0.0
    private var y: Double = 0.0
    private var operand = ""
    private var canAddUnaryMinus = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calcDisplay = findViewById(R.id.displayEditText)
        calcDisplay.inputType = InputType.TYPE_NULL
        calcDisplay.text.clear()

        val zero: Button = findViewById<Button>(R.id.button0)

        zero.setOnClickListener {
            if (calcDisplay.text.toString() != "0") zero.appendToDisplayOnClick()
        }
        setupButton(R.id.button1)
        setupButton(R.id.button2)
        setupButton(R.id.button3)
        setupButton(R.id.button4)
        setupButton(R.id.button5)
        setupButton(R.id.button6)
        setupButton(R.id.button7)
        setupButton(R.id.button8)
        setupButton(R.id.button9)

        val clear: Button = findViewById(R.id.clearButton)
        val dot: Button = findViewById(R.id.dotButton)
        val equal: Button = findViewById(R.id.equalButton)

        val minus: Button = findViewById(R.id.minusButton)
        val plus: Button = findViewById(R.id.addButton)
        val multiply: Button = findViewById(R.id.multiplyButton)
        val divide: Button = findViewById(R.id.divideButton)

        clear.setOnClickListener {
            calcDisplay.text.clear()
            calcDisplay.hint = "0"
            x = 0.0
            y = 0.0
            operand = ""
            canAddUnaryMinus = true
        }

        dot.setOnClickListener {
            if (calcDisplay.text.isEmpty() || calcDisplay.text.toString().first() == '-') {
                calcDisplay.text.append("0.")
            } else if (!calcDisplay.text.toString().contains(".")) {
                calcDisplay.text.append(".")
            }
        }

        equalize(equal)

        performOperation(minus, equal)
        performOperation(plus, equal)
        performOperation(multiply, equal)
        performOperation(divide,equal)
    }

    private fun equalize(equal: Button): Double {
        var result: Double = 0.0

        equal.setOnClickListener {
            if (calcDisplay.text.isNotEmpty() && calcDisplay.text.toString() != "-") {
                y = calcDisplay.text.toString().toDouble()
            } else if(y == 0.0) {
                y = x
            }

            Log.i(x.toString(), "iz equal x: $x")

            if (y == 0.0) {
                calcDisplay.text.clear()
            } else {
                calcDisplay.text.clear()
                result = when (operand) {
                    "-" -> x - y
                    "+" -> x + y
                    "*" -> x * y
                    "/" -> x / y
                    else -> y
                }
                Log.i(result.toString(), "result from $x $operand $y equal: $result")

                if(floor(result) == result)
                    calcDisplay.hint = result.toInt().toString()
                else
                    calcDisplay.hint = result.toString()

                x = result
                canAddUnaryMinus = false
            }
        }
        return result
    }

    private fun performOperation(button: Button, equal: Button) {
        button.setOnClickListener {
            y = 0.0
            if( button.id == R.id.minusButton && calcDisplay.text.isEmpty() && canAddUnaryMinus ) {
                calcDisplay.text.append("-")
                canAddUnaryMinus = false
            } else {
                x = if (calcDisplay.text.isNotEmpty()) {
                    calcDisplay.text.toString().toDouble()
                } else if (calcDisplay.hint.isNotEmpty()) {
                    calcDisplay.hint.toString().toDouble()
                } else {
                    0.0
                }
                operand = button.text.toString()
                canAddUnaryMinus = true
                Log.i(x.toString(), "perform operation $operand to x: $x")
                calcDisplay.text.clear()
                //calcDisplay.hint = String.format("%1s %2s", x.toString(), operand)
                if(floor(x) == x)
                    calcDisplay.hint = x.toInt().toString()
                else
                    calcDisplay.hint = x.toString()

                equalize(equal)
            }
        }
    }

    private fun Button.appendToDisplayOnClick() {
        if (calcDisplay.text.toString() == "0") calcDisplay.text.clear()

        if(calcDisplay.text.contains("-0") && !calcDisplay.text.contains(".")){
            calcDisplay.text.clear()
            calcDisplay.text.append("-$text")
        }else {
            calcDisplay.text.append(text)
        }
    }

    private fun setupButton(@IdRes resId: Int): Button =
        findViewById<Button>(resId).apply {
            this.setOnClickListener {
                appendToDisplayOnClick()
            }
        }
}