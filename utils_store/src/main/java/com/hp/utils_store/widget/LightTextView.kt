package com.hp.utils_store.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
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
    private var drawablePadding: Int = 0

    private lateinit var showTextRect: Rect
    private lateinit var textRect: Rect
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
        drawablePadding = a.getDimensionPixelSize(R.styleable.LightTextView_drawablePadding, drawablePadding)

        textRect = Rect()
        showTextRect = Rect()
        mPaint = Paint()
    }

    override fun onDraw(canvas: Canvas?) {
        if(canvas == null) return

        LogUtil.d(getClassName(), "onDraw")
        super.onDraw(canvas)

        //绘制drawable
        drawableLeft?.draw(canvas)
        drawableRight?.draw(canvas)

        //绘制文本
        // 计算出基线和到文字中间的距离
        val offset = Math.abs(mPaint.ascent() + mPaint.descent()) / 2
        mPaint.setColor(mTextColor)
        canvas.drawText(showText, showTextRect.left.toFloat(), measuredHeight.toFloat() / 2 + offset, mPaint)
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

        setTextRawBounds()
        setDrawableRawBounds()
        //测量View的宽度
        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize
        }else{
            width = paddingLeft + getDrawableWidthSpace(drawableLeft) + textRect.width() +
                    getDrawableWidthSpace(drawableRight) + paddingRight
            //最大不能超过父控件的约束尺寸
            width = Math.min(width, widthSize)
        }
        //测量高度
        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize
        }else{
            var drawableHeight = 0
            if(drawableLeft != null || drawableRight != null){
                drawableHeight = Math.max(getDrawableHeightSpace(drawableLeft), getDrawableHeightSpace(drawableRight))
            }
            height = paddingTop + Math.max(textRect.height(), drawableHeight) + paddingBottom
            //最大不能超过父控件的约束尺寸
            height = Math.min(height, heightSize)
        }

        //测量可绘制文本的宽度，计算出能显示的文本
        val showTextWidth = width - paddingLeft - paddingRight -
                getDrawableWidthSpace(drawableLeft) - getDrawableWidthSpace(drawableRight)
        showText = if(textRect.width() > showTextWidth){
            var endIndex = ((showTextWidth.toFloat() / textRect.width()) * mText.length).toInt()
            var result = mText.subSequence(0, endIndex).toString() + SUFFIX
            mPaint.textSize = mTextSize.toFloat()
            mPaint.getTextBounds(result, 0, result.length, showTextRect)
            //当加上后缀之后大于可绘制文字的宽度，则进行尾部字符截取
            while (showTextRect.width() > showTextWidth){
                endIndex--
                result = mText.subSequence(0, endIndex).toString() + SUFFIX
                mPaint.getTextBounds(result, 0, result.length, showTextRect)
            }
            result
        }else{
            showTextRect = textRect
            mText
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        //给drawable布局，计算x轴和y轴的偏移量
        drawableLeft?.let{
            val xOffset = paddingLeft
            val yOffset = (measuredHeight - drawableLeft!!.bounds.height()) / 2
            val rect = drawableLeft!!.bounds
            drawableLeft!!.setBounds(rect.left + xOffset, rect.top + yOffset,
                    rect.right + xOffset, rect.bottom + yOffset)
        }
        drawableRight?.let {
            val xOffset = measuredWidth - paddingRight - drawableRight!!.bounds.width()
            val yOffset = (measuredHeight - drawableRight!!.bounds.height()) / 2
            val rect = drawableRight!!.bounds
            drawableRight!!.setBounds(rect.left + xOffset, rect.top + yOffset,
                    rect.right + xOffset, rect.bottom + yOffset)
        }

        //给文本布局
        if(!TextUtils.isEmpty(mText)){
            val xOffset = paddingLeft + getDrawableWidthSpace(drawableLeft)
            val yOffset = paddingTop
            showTextRect.offset(xOffset, yOffset)
        }
    }

    private fun setTextRawBounds(){
        mPaint.textSize = mTextSize.toFloat()
        mPaint.getTextBounds(mText, 0, mText.length, textRect)
    }

    private fun setDrawableRawBounds(xOffset: Int = 0, yOffset: Int = 0){
        drawableLeft?.setBounds(0, 0, drawableLeft!!.intrinsicWidth, drawableLeft!!.intrinsicHeight)
        drawableRight?.setBounds(0, 0, drawableRight!!.intrinsicWidth, drawableRight!!.intrinsicHeight)
    }

    private fun getDrawableWidthSpace(drawable: Drawable?): Int{
        return if(drawable != null){
            drawable!!.bounds?.width() + drawablePadding
        }else 0
    }

    private fun getDrawableHeightSpace(drawable: Drawable?) = drawable?.bounds?.height() ?: 0

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

    fun setCompoundDrawablesWithIntrinsicBounds(@DrawableRes left: Int = 0, @DrawableRes right: Int = 0){
        setCompoundDrawablesWithIntrinsicBounds(if(left != 0) context.getDrawable(left) else null,
                if(right != 0) context.getDrawable(right) else null)
    }

    fun setCompoundDrawablesWithIntrinsicBounds(left: Drawable?, right: Drawable?){
        drawableLeft = left
        drawableRight = right
        requestLayout()
    }

    fun setCompoundDrawablePadding(padding: Int){
        if(padding != drawablePadding){
            drawablePadding = padding
            requestLayout()
        }
    }

}