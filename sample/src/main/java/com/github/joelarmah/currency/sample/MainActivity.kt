package com.github.joelarmah.currency.sample

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.joelarmah.currency.CurrencySymbols
import com.github.joelarmah.currency.sample.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mActivity: Activity
    private lateinit var mContext: Context
    private lateinit var viewBinding: ActivityMainBinding

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        mActivity = this
        mContext = this

        viewBinding.currencyEditText.setCurrency(CurrencySymbols.USA)
        viewBinding.currencyEditText.setDelimiter(false)
        viewBinding.currencyEditText.setSpacing(false)
        viewBinding.currencyEditText.setDecimals(true)
        viewBinding.currencyEditText.setSeparator(".")

        viewBinding.btnProcess.setOnClickListener {
            if (viewBinding.currencyEditText.length() != 0) {
                val cleanDoubleOutput = viewBinding.currencyEditText.cleanDoubleValue
                val cleanIntOutput = viewBinding.currencyEditText.cleanIntValue

                writeLog("Clean Double  : $cleanDoubleOutput")
                writeLog("Clean Integer : $cleanIntOutput")
            } else {
                Toast.makeText(mContext, "Input cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun writeLog(msg: String) {
        runOnUiThread {
            val now = Date()
            viewBinding.tvLogArea.append(
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SS",
                    Locale("en", "MY")
                ).format(now) + "\n" + msg + "\n\n"
            )
            Log.d(
                TAG,
                "writeLog: " + SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SS",
                    Locale("en", "MY")
                ).format(now) + "\n" + msg + "\n\n"
            )
        }
    }
}