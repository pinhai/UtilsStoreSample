package com.hp.utils_store.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.DrawableUtils
import com.hp.utils_store.R
import com.hp.utils_store.utils.LogUtil
import com.hp.utils_store.utils.getClassName

/**
 * 自定义轻量化TextView，只显示单行
 * Author：admin_h on 2021/4/20 20:10
 */
class LightTextView : View {

    companion object{
        //如果无法显示全部字数，就显示该后缀
        private const val SUFFIX = "..."
    }

    private var mText: String = ""
    //将显示出来的文本
    private var showText: String = ""
    private var mTextColor: Int = 0
    private var mTextSize: Int = 0
    private var drawableLeft: Drawable? = null
    private var drawableRight: Drawable? = null

    private lateinit var rect: Rect
    private lateinit var mPaint: Paint

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes){
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.LightTextView, defStyleAttr, defStyleRes)
        mText = a.getString(R.styleable.LightTextView_text) ?: ""
        mTextColor = a.getColor(R.styleable.LightTextView_textColor, Color.BLACK)
        mTextSize = a.getDimensionPixelSize(R.styleable.LightTextView_textSize,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16F, getResources().getDisplayMetrics()).toInt()
        )
        drawableLeft = a.getDrawable(R.styleable.LightTextView_drawableLeft)
        drawableRight = a.getDrawable(R.styleable.LightTextView_drawableRight)

        rect = Rect()
        mPaint = Paint()
        getTextBounds()
    }

    override fun onDraw(canvas: Canvas?) {
        if(canvas == null) return

        LogUtil.d(getClassName(), "onDraw")
        super.onDraw(canvas)

        //绘制drawable
        if(drawableLeft != null){
//            val bitmap = Drawable
//            canvas.drawBitmap()
        }

        //绘制文本
        // 计算出基线和到文字中间的距离
        val offset = Math.abs(mPaint.ascent() + mPaint.descent()) / 2
        val x = paddingLeft
        mPaint.setColor(mTextColor)
        canvas.drawText(showText, x.toFloat(), height.toFloat() / 2 + offset, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        LogUtil.d(getClassName(), "onMeasure")
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width: Int
        var height: Int

        LogUtil.v(getClassName(), "widthMode:$widthMode, widthSize:$widthSize")

        //测量View的宽度
        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize
        }else{
            getTextBounds()
            width = paddingLeft + rect.width() + paddingRight
            //最大不能超过父View
            width = Math.min(width, widthSize)
        }
        //测量高度
        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize
        }else{
            getTextBounds()
            height = paddingTop + rect.height() + paddingBottom
            //最大不能超过父View
            height = Math.min(height, heightSize)
        }

        //测量可绘制文本的宽度，计算出能显示的文本
        val drawableWidth = measuredWidth - paddingLeft - paddingRight
        showText = if(rect.width() > drawableWidth){
            var endIndex = ((drawableWidth.toFloat() / rect.width()) * mText.length).toInt()
            var result = mText.subSequence(0, endIndex).toString() + SUFFIX
            val rect = Rect()
            mPaint.textSize = mTextSize.toFloat()
            mPaint.getTextBounds(result, 0, result.length, rect)
            //当加上后缀之后大于可绘制文字的宽度，则进行尾部字符截取
            while (rect.width() > drawableWidth){
                endIndex--
                result = mText.subSequence(0, endIndex).toString() + SUFFIX
                mPaint.getTextBounds(result, 0, result.length, rect)
            }
            result
        }else{
            mText
        }

        setMeasuredDimension(width, height)
    }

    private fun getTextBounds(){
        mPaint.textSize = mTextSize.toFloat()
        mPaint.getTextBounds(mText, 0, mText.length, rect)
    }

    fun setText(text: String?){
        if(text == null) return
        mText = text
        requestLayout()
    }

    fun getText() = mText

    fun setTextSize(textSize: Int){
        mTextSize = textSize
        requestLayout()
    }

    fun getTextSize() = mTextSize

    fun setTextColor(@ColorInt textColor: Int){
        mTextColor = textColor
        invalidate()
    }

    fun getTextColor() = mTextColor

}