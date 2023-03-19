package com.xwk.someview.password

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.setPadding
import com.xwk.someview.R
import com.xwk.someview.dpToPx
import java.lang.StringBuilder

class CustomPassword(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    constructor(
        context: Context, passwordNumber: Int, bgColor: Int,
        bgSize: Int, bgCorner: Int, divisionLineColor: Int, divisionLineSize: Int,
        passwordColor: Int, passwordRadius: Int, passwordHeight: Float
    ) : this(
        context,
        null,
        passwordNumber,
        bgColor,
        bgSize,
        bgCorner,
        divisionLineColor,
        divisionLineSize,
        passwordColor,
        passwordRadius,
        passwordHeight
    )

    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        passwordNumber: Int,
        bgColor: Int,
        bgSize: Int,
        bgCorner: Int,
        divisionLineColor: Int,
        divisionLineSize: Int,
        passwordColor: Int,
        passwordRadius: Int,
        passwordHeight: Float
    ) : this(
        context,
        attrs,
        0
    ) {
        this.passwordNumber = passwordNumber
        this.bgColor = bgColor
        this.bgSize = bgSize
        this.bgCorner = bgCorner
        this.divisionLineColor = divisionLineColor
        this.divisionLineSize = divisionLineSize
        this.passwordColor = passwordColor
        this.passwordRadius = passwordRadius
        this.passwordHeight = passwordHeight
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            passwordHeight.toInt()
        )
    }

    //密码距离边缘最小距离
    private val theMinScopeFromScreenOfPassword = 100

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 一个密码所占的宽度
    private var passwordItemWidth = 0

    // 密码的个数默认为6位数
    private var passwordNumber = 6

    // 背景边框颜色
    private var bgColor = Color.parseColor("#d1d2d6")

    // 背景边框大小
    private var bgSize = 1

    // 背景边框圆角大小
    private var bgCorner = 3

    // 分割线的颜色
    private var divisionLineColor = bgColor

    // 分割线的大小
    private var divisionLineSize = bgSize

    // 密码圆点的颜色
    private var passwordColor = divisionLineColor

    // 密码圆点的半径大小
    private var passwordRadius = 4

    private var passwordHeight = 30f

    override fun onDraw(canvas: Canvas) {
        passwordItemWidth = (width - theMinScopeFromScreenOfPassword * 2 - (passwordNumber + 1) * bgSize) / passwordNumber
        //画密码圆角边框
        drawPasswordFrame(canvas)
        //画密码中分割线
        drawPasswordLine(canvas)
        //画密码点
        drawPasswordCircle(canvas)
    }

    private fun drawPasswordFrame(canvas: Canvas) {
        paint.color = bgColor
        paint.strokeWidth = bgSize.toFloat()
        paint.style = Paint.Style.STROKE
        val roundRect = RectF(
            theMinScopeFromScreenOfPassword + bgSize.toFloat(), bgSize.toFloat(),
            (width - bgSize - theMinScopeFromScreenOfPassword).toFloat(), passwordHeight - bgSize
        )
        canvas.drawRoundRect(roundRect, bgCorner.toFloat(), bgCorner.toFloat(), paint)
    }

    private fun drawPasswordLine(canvas: Canvas) {
        paint.color = divisionLineColor
        for (i in 0 until passwordNumber - 1) {
            val startX = (i + 1) * passwordItemWidth + bgSize * (i + 1) + theMinScopeFromScreenOfPassword
            val startY = bgSize
            val stopY = height - bgSize
            canvas.drawLine(
                startX.toFloat(), startY.toFloat(),
                startX.toFloat(), stopY.toFloat(), paint
            )
        }
    }

    private fun drawPasswordCircle(canvas: Canvas) {
        val num = text.toString().trim()
        paint.color = passwordColor
        paint.style = Paint.Style.FILL
        paint.strokeWidth = passwordRadius.toFloat()
        for (i in num.indices) {
            val cx = bgSize * (i + 1) + passwordItemWidth * i + passwordItemWidth / 2 + theMinScopeFromScreenOfPassword
            val cy = height / 2
            canvas.drawCircle(cx.toFloat(), cy.toFloat(), passwordRadius.toFloat(), paint)
        }
    }

    @SuppressLint("SetTextI18n")
    fun addContent(content: String) {
        val originText = text
        if (originText!!.length == passwordNumber) {
            return
        }
        val newText = originText.toString() + content
        setText(newText)
    }

    fun deleteNum() {
        if (text!!.isEmpty()) {
            return
        }
        var originText = text.toString()
        if (originText.isNotBlank()) {
            originText = originText.substring(0, originText.length - 1)
        }
        setText(originText)
    }

}