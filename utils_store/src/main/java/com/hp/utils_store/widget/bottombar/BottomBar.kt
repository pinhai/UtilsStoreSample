package com.hp.utils_store.widget.bottombar

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import com.hp.utils_store.R
import java.util.*

class BottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TRANSLATE_DURATION_MILLIS = 200
    }

    init {
        init(context, attrs)
    }

    private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
    private var isVisible = true
    private val mTabs: MutableList<BottomBarTab> = ArrayList()
    private var mTabLayout: LinearLayout? = null
    private var mTabParams: LayoutParams? = null
    private var currentItemPosition = 0
    private var mListener: OnTabSelectedListener? = null

    //被选中和未选中状态的tab文本颜色，如果设置了则所有tab文本都使用该配色
    private var titleColorSelected: Int? = null
    private var titleColorUnselected: Int? = null

    private fun init(context: Context, attrs: AttributeSet?) {
        orientation = VERTICAL

//        ImageView shadowView = new ImageView(context);
//        shadowView.setBackgroundResource(R.drawable.actionbar_shadow_up);
//        addView(shadowView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTabLayout = LinearLayout(context)
        mTabLayout!!.setBackgroundColor(Color.WHITE)
        mTabLayout!!.orientation = HORIZONTAL
        addView(
            mTabLayout,
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        mTabParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        mTabParams!!.weight = 1f
    }

    /**
     * 设置tab选中和未选中状态的文本颜色，在{@link #addItem(com.hp.utils_store.widget.bottombar.BottomBarTab)}之前调用
     */
    fun setTitleColor(@ColorInt titleColorSelected: Int, @ColorInt titleColorUnselected: Int): BottomBar {
        this.titleColorSelected = titleColorSelected
        this.titleColorUnselected = titleColorUnselected
        return this
    }

    fun addItem(tab: BottomBarTab): BottomBar {
        if(titleColorSelected != null && titleColorUnselected != null){
            tab.setTitleColor(titleColorSelected!!, titleColorUnselected!!)
        }
        tab.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mListener == null) return
                val pos = tab.tabPosition
                if (currentItemPosition == pos) {
                    mListener!!.onTabReselected(pos)
                } else {
                    mListener!!.onTabSelected(pos, currentItemPosition)
                    tab.isSelected = true
                    mListener!!.onTabUnselected(currentItemPosition)
                    mTabs[currentItemPosition].isSelected = false
                    currentItemPosition = pos
                }
            }
        })
        tab.tabPosition = mTabLayout!!.childCount
        tab.layoutParams = mTabParams
        mTabLayout!!.addView(tab)
        mTabs.add(tab)
        return this
    }

    fun setOnTabSelectedListener(onTabSelectedListener: OnTabSelectedListener?) {
        mListener = onTabSelectedListener
    }

    fun setCurrentItem(position: Int) {
        mTabLayout!!.postDelayed({ mTabLayout!!.getChildAt(position).performClick() }, 100)
    }

    /**
     * 获取 Tab
     */
    fun getItem(index: Int): BottomBarTab? {
        return if (mTabs.size < index) null else mTabs[index]
    }

    interface OnTabSelectedListener {
        fun onTabSelected(position: Int, prePosition: Int)
        fun onTabUnselected(position: Int)
        fun onTabReselected(position: Int)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, currentItemPosition)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        if (currentItemPosition != ss.position) {
            mTabLayout!!.getChildAt(currentItemPosition).isSelected = false
            mTabLayout!!.getChildAt(ss.position).isSelected = true
        }
        currentItemPosition = ss.position
    }

    internal class SavedState : BaseSavedState {
        var position: Int

        constructor(source: Parcel) : super(source) {
            position = source.readInt()
        }

        constructor(superState: Parcelable?, position: Int) : super(superState) {
            this.position = position
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(position)
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState? {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    @JvmOverloads
    fun hide(anim: Boolean = true) {
        toggle(false, anim, false)
    }

    @JvmOverloads
    fun show(anim: Boolean = true) {
        toggle(true, anim, false)
    }

    private fun toggle(visible: Boolean, animate: Boolean, force: Boolean) {
        if (isVisible != visible || force) {
            isVisible = visible
            val height = height
            if (height == 0 && !force) {
                val vto = viewTreeObserver
                if (vto.isAlive) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            val currentVto = viewTreeObserver
                            if (currentVto.isAlive) {
                                currentVto.removeOnPreDrawListener(this)
                            }
                            toggle(visible, animate, true)
                            return true
                        }
                    })
                    return
                }
            }
            val translationY = if (visible) 0 else height
            if (animate) {
                animate().setInterpolator(mInterpolator)
                    .setDuration(TRANSLATE_DURATION_MILLIS.toLong())
                    .translationY(translationY.toFloat())
            } else {
                ViewCompat.setTranslationY(this, translationY.toFloat())
            }
        }
    }

}