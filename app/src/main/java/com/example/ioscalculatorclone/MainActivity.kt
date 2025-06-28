package com.example.ioscalculatorclone

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ioscalculatorclone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

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

        numberButtons.forEach { btn ->
            btn.setOnClickListener {
                viewModel.onNumberClick(btn.text.toString())
            }
        }

        binding.apply {
            btnDot.setOnClickListener {
                viewModel.onNumberClick(".")
            }

            btnPlus.setOnClickListener {
                if (viewModel.lastResult == "Undefined") {
                    return@setOnClickListener
                } else {
                    viewModel.onOperatorClick("+")
                }
            }
            btnMinus.setOnClickListener {
                if (viewModel.lastResult == "Undefined") {
                    return@setOnClickListener
                } else {
                    viewModel.onOperatorClick("-")
                }
            }
            btnMultiply.setOnClickListener {
                if (viewModel.lastResult == "Undefined") {
                    return@setOnClickListener
                } else {
                    viewModel.onOperatorClick("*")
                }
            }
            btnDivide.setOnClickListener {
                if (viewModel.lastResult == "Undefined") {
                    return@setOnClickListener
                } else {
                    viewModel.onOperatorClick("/")
                }
            }
            btnEquals.setOnClickListener {
                if (viewModel.lastResult == "Undefined" || !viewModel.currentInput.contains(
                        Regex("[+\\-*/%]")
                    ) || viewModel.currentInput.matches(Regex(".*[+\\-*/]$"))
                    || viewModel.lastOperation == "minusNumber"
                ) {
                    return@setOnClickListener
                } else {
                    viewModel.onEqualsClick()
                    binding.writtenOperation.text = viewModel.lastOperation
                }

            }

            btnAC.setOnClickListener {
                viewModel.onClearClick()
            }
            removed.setOnClickListener {
                viewModel.onOperatorClick("%")
            }
            btnToast.setOnClickListener {
                Toast.makeText(this@MainActivity, "<Mr.ElshodDev/>", Toast.LENGTH_SHORT).show()

            }
        }
    }
}