package com.hp.utils_store.widget.bottombar

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.hp.utils_store.R

class BottomBarTab(
    context: Context,
    @DrawableRes icon: Int,
    title: CharSequence?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mIcon: ImageView? = null
    private var mTvTitle: TextView? = null
    private var mContext: Context? = null
    private var mTabPosition = -1
    private var titleColorSelected = R.color.tab //被选中状态的tab文本颜色
    private var titleColorUnselected = R.color.tab_unselected
    private var mTvUnreadCount: TextView? = null

    private fun init(context: Context, icon: Int, title: CharSequence?) {
        mContext = context
        val typedArray =
            context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackgroundBorderless))
        val drawable = typedArray.getDrawable(0)
        setBackgroundDrawable(drawable)
        typedArray.recycle()
        val lLContainer = LinearLayout(context)
        lLContainer.orientation = LinearLayout.VERTICAL
        lLContainer.gravity = Gravity.CENTER
        val paramsContainer =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        paramsContainer.gravity = Gravity.CENTER
        lLContainer.layoutParams = paramsContainer
        mIcon = ImageView(context)
        val size =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27f, resources.displayMetrics)
                .toInt()
        val params = LinearLayout.LayoutParams(size, size)
        mIcon!!.setImageResource(icon)
        mIcon!!.layoutParams = params
        //        mIcon.setColorFilter(ContextCompat.getColor(context, R.color.tab_unselect));
        lLContainer.addView(mIcon)
        mTvTitle = TextView(context)
        mTvTitle!!.text = title
        val paramsTv = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        paramsTv.topMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics)
                .toInt()
        mTvTitle!!.textSize = 10f
        //        mTvTitle.setTextColor(ContextCompat.getColor(context, R.color.tab_unselect));
        mTvTitle!!.layoutParams = paramsTv
        lLContainer.addView(mTvTitle)
        addView(lLContainer)
        val min = dip2px(context, 9f)
        val padding = dip2px(context, 3f)
        mTvUnreadCount = TextView(context)
        mTvUnreadCount!!.setBackgroundResource(R.drawable.shape_msg_bubble)
        mTvUnreadCount!!.minWidth = min
        mTvUnreadCount!!.setTextColor(Color.WHITE)
        mTvUnreadCount!!.setPadding(padding, 0, padding, 0)
        mTvUnreadCount!!.gravity = Gravity.CENTER
        val tvUnReadParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, min)
        tvUnReadParams.gravity = Gravity.CENTER
        tvUnReadParams.leftMargin = dip2px(context, 17f)
        tvUnReadParams.bottomMargin = dip2px(context, 14f)
        mTvUnreadCount!!.layoutParams = tvUnReadParams
        mTvUnreadCount!!.visibility = GONE
        addView(mTvUnreadCount)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            mTvTitle!!.setTextColor(ContextCompat.getColor(mContext!!, titleColorSelected))
        } else {
            mTvTitle!!.setTextColor(ContextCompat.getColor(mContext!!, titleColorUnselected))
        }
    }

    /**
     * 设置tab选中和未选中状态的文本颜色
     */
    fun setTitleColor(@ColorInt titleColorSelected: Int, @ColorInt titleColorUnselected: Int) {
        this.titleColorSelected = titleColorSelected
        this.titleColorUnselected = titleColorUnselected
    }

    var tabPosition: Int
        get() = mTabPosition
        set(position) {
            mTabPosition = position
            if (position == 0) {
                isSelected = true
            }
        }
    /**
     * 获取当前未读数量
     */
    /**
     * 设置未读数量
     * num为-100只显示一个小红点
     */
    var unreadCount: Int
        get() {
            var count = 0
            if (TextUtils.isEmpty(mTvUnreadCount!!.text)) {
                return count
            }
            if (mTvUnreadCount!!.text.toString() == "99+") {
                return 99
            }
            try {
                count = Integer.valueOf(mTvUnreadCount!!.text.toString())
            } catch (ignored: Exception) {
            }
            return count
        }
        set(num) {
            if (num <= 0) {
                if (num == -100) {
                    mTvUnreadCount!!.text = ""
                    mTvUnreadCount!!.visibility = VISIBLE
                } else {
                    mTvUnreadCount!!.text = 0.toString()
                    mTvUnreadCount!!.visibility = GONE
                }
            } else {
                mTvUnreadCount!!.visibility = VISIBLE
                if (num > 99) {
                    mTvUnreadCount!!.text = "99+"
                } else {
                    mTvUnreadCount!!.text = num.toString()
                }
            }
        }

    private fun dip2px(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    init {
        init(context, icon, title)
    }
}