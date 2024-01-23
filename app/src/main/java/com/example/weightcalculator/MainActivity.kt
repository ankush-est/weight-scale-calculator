package com.example.weightcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weightcalculator.databinding.ActivityMainBinding
import com.neostra.electronic.Electronic
import com.neostra.electronic.ElectronicCallback

enum class WeightStatus(val value: String) {
    OVERWEIGHT("46"), STABLE("53"), UNSTABLE("55"), SUCCESS_MANUAL_PEELING("56"), FAILED_MANUAL_PEELING(
        "57"
    )
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Electronic
    private var mElectronic: Electronic? = null

    //
    private var currentWeight: String? = null
    private var currentTareWeight: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeWeightScale()
        initializeButtons()
    }

    private fun initializeWeightScale() {
        val electronicCallback = ElectronicCallback { weight, weightStatus ->
            currentWeight = weight
            when (weightStatus) {
                WeightStatus.OVERWEIGHT.value -> {
                    binding.tvWeightStatus.text = WeightStatus.OVERWEIGHT.name
                }

                WeightStatus.STABLE.value -> {
                    binding.tvWeightStatus.text = WeightStatus.STABLE.name
                }

                WeightStatus.UNSTABLE.value -> {
                    binding.tvWeightStatus.text = WeightStatus.UNSTABLE.name
                }

            }
            binding.tvWeight.text = "$weight KG"
        }
        mElectronic = Electronic.Builder().setReceiveCallback(electronicCallback).builder()
    }

    private fun initializeButtons() {
        binding.btnZero.setOnClickListener {
            mElectronic?.turnZero()
            currentWeight = null
            currentTareWeight = null
            binding.tvWeight.text = "0.000 KG"
            binding.tvWeightStatus.text = "NA"
            binding.tvTareWeight.text = "0.000 KG"
        }
        binding.btnPeel.setOnClickListener {
            val weight = currentWeight?.toDouble() ?: 0.000
            if (currentWeight == null && weight == 0.000) {
                binding.tvTareWeight.text = "0.000 KG"
            } else if (weight > 0.000) {
                currentTareWeight = currentWeight
                mElectronic?.removePeel()
                binding.tvTareWeight.text = "$currentWeight KG"
            }
        }
    }
}