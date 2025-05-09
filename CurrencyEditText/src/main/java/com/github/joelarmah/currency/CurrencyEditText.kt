package com.github.joelarmah.currency

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

//class CurrencyEditText : AppCompatEditText {
class CurrencyEditText : TextInputEditText {
    private var current = ""
    private val editText = this

    private var mContext: Context? = null

    // properties
    private var currency = ""
    private var separator = ","
    private var spacing = false
    private var delimiter = false
    private var decimals = true

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        init(context, attrs, "", ",", spacing = false, delimiter = false, decimals = true)
    }

    private fun init(context: Context, attrs: AttributeSet?, currency: String, separator: String, spacing: Boolean, delimiter: Boolean, decimals: Boolean) {
        this.mContext = context
        this.currency = currency
        this.separator = separator
        this.spacing = spacing
        this.delimiter = delimiter
        this.decimals = decimals

        initByAttributes()
    }

    private fun initByAttributes() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    editText.removeTextChangedListener(this)

                    val cleanString = s.toString().replace(Regex("[$,.]"), "").replace(currency, "").replace("\\s+".toRegex(), "")

                    if (cleanString.isNotEmpty()) {
                        try {
                            val currencyFormat = if (spacing) {
                                if (delimiter) "$currency. " else "$currency "
                            } else {
                                if (delimiter) "$currency." else currency
                            }

                            val parsed: Double
                            val formatted: String

                            if (decimals) {
                                parsed = cleanString.toDouble()
                                formatted = NumberFormat.getCurrencyInstance(Locale.US).format(parsed / 100).replace("$" ?: "", currencyFormat)
                                // formatted = currencyFormat + NumberFormat.getNumberInstance(Locale.US).format(parsed)
                            } else {
                                val parsedInt = cleanString.toInt()
                                formatted = currencyFormat + NumberFormat.getNumberInstance(Locale.US).format(parsedInt)
                            }

                            current = formatted

                            if (separator != "," && !decimals) {
                                editText.setText(formatted.replace(",", separator))
                            } else {
                                editText.setText(formatted)
                            }
                            val selectionPosition = if (formatted.length <= editText.text?.length!!) formatted.length else editText.text?.length!!
                            editText.setSelection(selectionPosition)
                            // editText.setSelection(formatted.length)
                        } catch (e: NumberFormatException) {
                            Log.e("CurrencyEditText", e.toString())
                        }
                    } else {
                        current = currency // Add this to always show the currency symbol
                        editText.setText(current)
                        editText.setSelection(current.length)
                    }
                    editText.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    fun getCleanDoubleValue(): Double {
        return try {
            if (decimals) {
                editText.text.toString().trim { it <= ' ' }.replace(Regex("[$,]"), "").replace(currency, "").toDouble()
            } else {
                val cleanString = editText.text.toString().trim { it <= ' ' }.replace(Regex("[$,.]"), "").replace(currency, "").replace("\\s+".toRegex(), "")
                cleanString.toDouble()
            }
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun getCleanIntValue(): Int {
        return try {
            if (decimals) {
                val doubleValue = editText.text.toString().trim { it <= ' ' }.replace(Regex("[$,]"), "").replace(currency, "").toDouble()
                doubleValue.roundToInt()
            } else {
                val cleanString = editText.text.toString().trim { it <= ' ' }.replace(Regex("[$,.]"), "").replace(currency, "").replace("\\s+".toRegex(), "")
                cleanString.toInt()
            }
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun setDecimals(value: Boolean) {
        this.decimals = value
    }

    fun setCurrency(currencySymbol: String) {
        // this.currency = getCurrencySymbol(currencySymbol)
        this.currency = currencySymbol
    }

    fun setSpacing(value: Boolean) {
        this.spacing = value
    }

    fun setDelimiter(value: Boolean) {
        this.delimiter = value
    }

    fun setSeparator(value: String) {
        this.separator = value
    }

    fun getCurrencySymbol(currency: String): String {
        return try {
            val sJSON = CurrencyUtils.getJsonFromAssets(mContext!!)
            val jsonObject = JSONObject(sJSON)
            val jsonObjectCountry = jsonObject.getJSONObject(currency)
            jsonObjectCountry.getString("symbolNative")
        } catch (e: JSONException) {
            e.printStackTrace()
            ""
        }
    }

}
