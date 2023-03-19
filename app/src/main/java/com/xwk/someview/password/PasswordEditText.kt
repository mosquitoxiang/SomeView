package com.xwk.someview.password

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import com.xwk.someview.R

class PasswordEditText(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //距离容器顶部的位置
    private val passwordToTopHeight = dp2px(30)

    private lateinit var customPassword: CustomPassword
    private lateinit var customKeyBoard: CustomKeyBoard

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

    //密码框高度
    private var passwordHeight = dp2px(40)

    private var bottomKeyBoardHeight = 0
    private var bottomKeyBoardWidth = 0
    private var topPasswordWidth = 0
    private var topPasswordHeight = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        initAttributeSet(context, attrs)
        customPassword = CustomPassword(
            context,
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
        customKeyBoard = CustomKeyBoard(context).apply {
            onInputTextListener = {
                customPassword.addContent(it)
            }
            onDeleteNum = {
                customPassword.deleteNum()
            }
        }
        addView(customPassword)
        addView(customKeyBoard)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
//        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //测量子View的尺寸
        measureChild(customPassword, widthMeasureSpec, heightMeasureSpec)
        measureChild(customKeyBoard, widthMeasureSpec, heightMeasureSpec)
        //获取子View的尺寸
        topPasswordWidth = customPassword.measuredWidth
        topPasswordHeight = customPassword.measuredHeight
        bottomKeyBoardWidth = customKeyBoard.measuredWidth
        bottomKeyBoardHeight = customKeyBoard.measuredHeight

        val widthMeasureSpecChild = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
        val heightMeasureSpecChild = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        customKeyBoard.measure(widthMeasureSpecChild, heightMeasureSpecChild)
//        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        customPassword.layout(
            (width - topPasswordWidth) / 2,
            passwordToTopHeight.toInt(), topPasswordWidth, (passwordHeight + passwordToTopHeight).toInt()
        )
        customKeyBoard.layout(0, b - bottomKeyBoardHeight, r, b)
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText)
        divisionLineSize = array.getDimension(
            R.styleable.PasswordEditText_divisionLineSize,
            dp2px(divisionLineSize)
        ).toInt()
        passwordRadius =
            array.getDimension(R.styleable.PasswordEditText_passwordRadius, dp2px(passwordRadius))
                .toInt()
        bgSize = array.getDimension(R.styleable.PasswordEditText_bgSize, dp2px(bgSize)).toInt()
        bgCorner =
            array.getDimension(R.styleable.PasswordEditText_bgCorner, dp2px(bgCorner)).toInt()
        bgColor = array.getColor(R.styleable.PasswordEditText_bgColor, bgColor)
        divisionLineColor =
            array.getColor(R.styleable.PasswordEditText_divisionLineColor, divisionLineColor)
        passwordColor =
            array.getColor(R.styleable.PasswordEditText_passwordColor, divisionLineColor)
        passwordNumber = array.getInteger(R.styleable.PasswordEditText_passwordNumber, 6)
        passwordHeight = array.getDimension(R.styleable.PasswordEditText_passwordHeight, dp2px(40))
        array.recycle()
    }

    private fun dp2px(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(), resources.displayMetrics
        ).toInt().toFloat()
    }
}