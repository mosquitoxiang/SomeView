package com.xwk.someview.proportion

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.xwk.someview.dpToPx
import com.xwk.someview.spToPx

class PercentageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //上方数字
    private var aboveTexts: MutableList<String> = mutableListOf("0.2", "0.4")

    //上方数字颜色
    private var aboveColors: MutableList<String> = mutableListOf("#53C258", "#C25A29")

    //下方文字
    private var belowTexts: MutableList<String> = mutableListOf("学习", "娱乐", "锻炼")

    //下方文字颜色
    private var belowColors: MutableList<String> = mutableListOf("#53C258", "#C25A29", "#26B3C2")

    //进度条颜色
    private var barColors: MutableList<String> = mutableListOf("#53C258", "#C25A29", "#26B3C2")

    //各部分占比
    private var percentages: MutableList<Float> = mutableListOf(0.2f, 0.4f, 0.4f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var countStart = 0f
    private var countEnd = 0f
    private var barHeight = 10.dpToPx(context)

    override fun onDraw(canvas: Canvas) {
        //圆弧半径
        val arcWidth = (barHeight / 2).toFloat()
        val totalRectWidth = measuredWidth - barHeight
        val barTop = ((height / 2) - (barHeight / 2)).toFloat()
        val barBottom = ((height / 2) + (barHeight / 2)).toFloat()
        val aboveTextTop = barTop - 10.dpToPx(context)
        val belowTextBottom = barBottom + 10.dpToPx(context)
        for (i in barColors.indices) {
            when (i) {
                0 -> {
                    //下方的文字
                    val text = belowTexts[i]
                    paint.color = Color.parseColor(belowColors[i])
                    paint.textSize = 10.spToPx(context)
                    val bounds = Rect()
                    paint.getTextBounds(text, 0, text.length, bounds)
                    val textWidth = bounds.width()
                    val offsetX = belowTextBottom - textWidth / 2
                    val offsetY = belowTextBottom + bounds.height() / 2
                    canvas.drawText(text, offsetX, offsetY, paint)
                    //左半圆弧
                    paint.color = Color.parseColor(barColors[0])
                    canvas.drawArc(
                        0f, barTop,
                        barHeight.toFloat(), barBottom,
                        90f, 180f, true, paint
                    )
                    canvas.drawRect(
                        arcWidth,
                        barTop,
                        (percentages[i] * totalRectWidth) + arcWidth,
                        barBottom,
                        paint
                    )
                    //第二块矩形的起始位置
                    countStart += totalRectWidth * percentages[i] + arcWidth
                    //第二块矩形的终止位置
                    countEnd += (percentages[0] + percentages[1]) * totalRectWidth + arcWidth
                }
                barColors.size - 1 -> {
                    //上方的文字
                    paint.color = Color.parseColor(aboveColors[i - 1])
                    paint.textSize = 10.spToPx(context)
                    val bounds = Rect()
                    val text = aboveTexts[i - 1]
                    paint.getTextBounds(text, 0, text.length, bounds)
                    val textWidth = bounds.width()
                    val offsetX = countStart - textWidth / 2
                    val offsetY = aboveTextTop + bounds.height() / 2
                    canvas.drawText(text, offsetX, offsetY, paint)
                    //下方的文字
                    val textBelow = belowTexts[i]
                    paint.color = Color.parseColor(belowColors[i])
                    paint.textSize = 10.spToPx(context)
                    val boundsBelow = Rect()
                    paint.getTextBounds(textBelow, 0, textBelow.length, boundsBelow)
                    val textWidthBelow = boundsBelow.width()
                    val offsetXBelow =
                        ((countEnd - countStart) / 2 + countStart) - textWidthBelow / 2
                    val offsetYBelow = belowTextBottom + boundsBelow.height() / 2
                    canvas.drawText(textBelow, offsetXBelow, offsetYBelow, paint)

                    paint.color = Color.parseColor(barColors.last())
                    canvas.drawRect(
                        countStart, barTop,
                        countEnd, barBottom, paint
                    )
                    canvas.drawArc(
                        countEnd - arcWidth,
                        barTop,
                        countEnd + arcWidth,
                        barBottom,
                        270f,
                        180f,
                        true,
                        paint
                    )
                }
                else -> {
                    //上方的文字
                    paint.color = Color.parseColor(aboveColors[i - 1])
                    paint.textSize = 10.spToPx(context)
                    val bounds = Rect()
                    val text = aboveTexts[i - 1]
                    paint.getTextBounds(text, 0, text.length, bounds)
                    val textWidth = bounds.width()
                    val offsetX = countStart - textWidth / 2
                    val offsetY = aboveTextTop + bounds.height() / 2
                    canvas.drawText(aboveTexts[i - 1], offsetX, offsetY, paint)
                    //下方的文字
                    val textBelow = belowTexts[i]
                    paint.color = Color.parseColor(belowColors[i])
                    paint.textSize = 10.spToPx(context)
                    val boundsBelow = Rect()
                    paint.getTextBounds(textBelow, 0, textBelow.length, boundsBelow)
                    val textWidthBelow = boundsBelow.width()
                    val offsetXBelow =
                        ((countEnd - countStart) / 2 + countStart) - textWidthBelow / 2
                    val offsetYBelow = belowTextBottom + boundsBelow.height() / 2
                    canvas.drawText(textBelow, offsetXBelow, offsetYBelow, paint)

                    paint.color = Color.parseColor(barColors[i])
                    canvas.drawRect(
                        countStart,
                        barTop,
                        countEnd,
                        barBottom,
                        paint
                    )
                    countStart += totalRectWidth * percentages[i]
                    countEnd += percentages[i] * totalRectWidth
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var maxWidth = resources.displayMetrics.widthPixels - 40.dpToPx(context)
        maxWidth = if (widthSize > maxWidth) {
            maxWidth
        } else {
            widthSize
        }
        var maxHeight = maxWidth
        maxHeight = if (heightSize > maxHeight) {
            maxHeight
        } else {
            heightSize
        }
        setMeasuredDimension(maxWidth, maxHeight)
        //对应match_parent,或具体的数值
        if (widthMode == MeasureSpec.EXACTLY) {
//            setMeasuredDimension()
        }
        //对应wrap_content
        if (widthMode == MeasureSpec.AT_MOST) {

        }
        if (widthMode == MeasureSpec.UNSPECIFIED) {

        }
    }

}