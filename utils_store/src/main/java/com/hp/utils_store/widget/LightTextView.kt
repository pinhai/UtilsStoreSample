package com.hp.utils_store.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.hp.utils_store.R
import com.hp.utils_store.utils.ScreenUtil

/**
 * 自定义轻量化TextView
 * Author：admin_h on 2021/4/20 20:10
 */
class LightTextView : View {

    private var mText: String = ""
    private var mTextColor: Int = 0
    private var mTextSize: Int = 0

    private lateinit var rect: Rect
    private lateinit var mPaint: Paint

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ){
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LightTextView,
            defStyleAttr,
            defStyleRes
        )
        mText = a.getString(R.styleable.LightTextView_text) ?: ""
        mTextColor = a.getColor(R.styleable.LightTextView_textColor, Color.BLACK)
        mTextSize = a.getDimensionPixelSize(
            R.styleable.LightTextView_textSize,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                16F,
                getResources().getDisplayMetrics()
            ).toInt()
        )

        rect = Rect()
        mPaint = Paint()
        mPaint.textSize = mTextSize.toFloat()
        mPaint.getTextBounds(mText, 0, mText.length, rect)
    }

    override fun onDraw(canvas: Canvas?) {
        if(canvas == null) return

        mPaint.setColor(Color.YELLOW)
        canvas.drawRect(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat(), mPaint)

        mPaint.setColor(mTextColor)
        // 计算出基线和到文字中间的距离
        val offset = Math.abs(mPaint.ascent() + mPaint.descent()) / 2
        canvas.drawText(mText,
            width.toFloat() / 2 - rect.width().toFloat() / 2,
            height.toFloat() / 2 + offset,
            mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width = 0
        var height = 0

        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize
        }else{
            mPaint.textSize = mTextSize.toFloat()
            mPaint.getTextBounds(mText, 0, mText.length, rect)
            width = paddingLeft + rect.width() + paddingRight
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize
        }else{
            mPaint.textSize = mTextSize.toFloat()
            mPaint.getTextBounds(mText, 0, mText.length, rect)
            height = paddingTop + rect.height() + paddingBottom
        }

        setMeasuredDimension(width, height)
    }

}