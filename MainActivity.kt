package com.example.tippy2

import android.os.Bundle
import android.util.Log
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.animation.ArgbEvaluator
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG="MainActivity"
private const val INITIAL_TIP_PERCENT=20
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress= INITIAL_TIP_PERCENT
        tvTipPercentLabel.text="$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text="$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG,"afterTextChanges $s")
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent){
            in 0..9->"Poor"
            in 10..14->"Average"
            in 15..22->"Good"
            in 23..30->"Great"
            else ->"Amazing"
        }
        tvTipDescription.text=tipDescription
        val color=ArgbEvaluator().evaluate(
            tipPercent.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this,R.color.worst),
            ContextCompat.getColor(this,R.color.best)
        )as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text=""
            tvTotalAmount.text=""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount=baseAmount*tipPercent/100
        val totalAmount=baseAmount+tipAmount
        tvTipAmount.text= "%.2f".format(tipAmount)
        tvTotalAmount.text="%.2f".format(totalAmount)
    }
}