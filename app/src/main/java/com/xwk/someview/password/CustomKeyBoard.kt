package com.xwk.someview.password

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.xwk.someview.R

class CustomKeyBoard(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.ui_customer_keyboard, this)
        setOnItemClickListener(this)
    }

    private fun setOnItemClickListener(view: View) {
        if (view is ViewGroup) {
            val childCount = view.childCount
            for (i in 0 until childCount) {
                val childView = view.getChildAt(i)
                setOnItemClickListener(childView)
            }
        } else {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        if (v is TextView) {
            val text = v.text.toString().trim()
            onInputTextListener?.invoke(text)
        }
        if (v is ImageView) {
            onDeleteNum?.invoke()
        }
    }

    var onInputTextListener: ((text: String) -> Unit)? = null
    var onDeleteNum: (() -> Unit)? = null
}