package com.example.ioscalculatorclone

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ioscalculatorclone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.display.observe(
            this, Observer {
                binding.tvDisplay.text = it
            }
        )
        viewModel.operation.observe(
            this, Observer {
                binding.writtenOperation.text = it
            }
        )
        val numberButtons = listOf<Button>(
            binding.btn0,
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9
        )
        val operatorButtons = listOf<Button>(
            binding.btnPlus,
            binding.btnEquals,
            binding.btnDivide,
            binding.btnMinus,
            binding.btnMultiply
        )
        numberButtons.forEach { btn ->
            btn.setOnClickListener {
                viewModel.onNumberClick(btn.text.toString())
            }
        }

        binding.btnDot.setOnClickListener {
            viewModel.onNumberClick(".")
        }

        binding.btnPlus.setOnClickListener {
            if (viewModel.lastResult == "Undefined") {
                return@setOnClickListener
            } else {
                viewModel.onOperatorClick("+")
            }
        }
        binding.btnMinus.setOnClickListener {
            if (viewModel.lastResult == "Undefined") {
                return@setOnClickListener
            } else {
                viewModel.onOperatorClick("-")
            }
        }
        binding.btnMultiply.setOnClickListener {
            if (viewModel.lastResult == "Undefined") {
                return@setOnClickListener
            } else {
                viewModel.onOperatorClick("*")
            }
        }
        binding.btnDivide.setOnClickListener {
            if (viewModel.lastResult == "Undefined") {
                return@setOnClickListener
            } else {
                viewModel.onOperatorClick("/")
            }
        }
        binding.btnEquals.setOnClickListener {
            if (viewModel.lastResult == "Undefined" || !viewModel.currentInput.contains(
                    Regex("[+\\-*/]")
                ) || viewModel.currentInput.matches(Regex(".*[+\\-*/]$"))
                || viewModel.lastOperation=="minusNumber"
                    ) {
                return@setOnClickListener
            } else {
                viewModel.onEqualsClick()
                binding.writtenOperation.text = viewModel.lastOperation
            }

        }

        binding.btnAC.setOnClickListener {
            viewModel.onClearClick()
        }
    }
}