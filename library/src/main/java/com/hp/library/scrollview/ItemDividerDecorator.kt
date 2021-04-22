package com.hp.library.scrollview

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * RecyclerView的item之间的分割线
 */
class ItemDividerDecorator : ItemDecoration {
    private var mDivider: Drawable?
    private var mDividerSize: Int
    private var mPaint: Paint? = null

    companion object {
        private val ATTRS = intArrayOf(R.attr.listDivider)
    }

    constructor(context: Context, color: Int = -1) {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        mDividerSize = mDivider!!.intrinsicHeight
        a.recycle()

        if(color > 0){
            //创建画笔
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            //设置画笔颜色
            mPaint!!.color = color
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawRightDivider(c, parent)
        drawBottomDivider(c, parent)
    }

    private fun drawRightDivider(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + mDividerSize
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            if (mPaint != null) {
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint!!)
            } else {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(c)
            }
        }
    }

    private fun drawBottomDivider(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val right = child.right - params.rightMargin + mDividerSize
            val top = child.bottom + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child))
            val bottom = top + mDividerSize
            if (mPaint != null) {
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint!!)
            } else {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(c)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val spanCount = getSpanCount(parent)
        if (spanCount == -1) throw ClassCastException("Can not cast" + parent.layoutManager + "to GridLayoutManager")
        if(isLastRaw(position, spanCount, parent.adapter!!.itemCount) && isLastColumn(position, spanCount)){
          //如果是最后一行最后一列，底部和右边都不绘制
            outRect.set(0, 0, 0, 0)
        } else if (isLastRaw(position, spanCount, parent.adapter!!.itemCount)) {
            //如果是最后一行不绘制底部
            outRect.set(0, 0, mDividerSize, 0)
        } else if (isLastColumn(position, spanCount)) {
            //如果是最后一列则不绘制右边的Divider
            outRect.set(0, 0, 0, mDividerSize)
        } else {
            outRect.set(0, 0, mDividerSize, mDividerSize)
        }
    }

    private fun isLastColumn(itemPosition: Int, spanCount: Int): Boolean {
        return (itemPosition + 1) % spanCount == 0
    }

    private fun isLastRaw(itemPosition: Int, spanCount: Int, childCount: Int): Boolean {
        return itemPosition >= childCount - childCount % spanCount
    }

    //获取列数
    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if (layoutManager is GridLayoutManager) {
            layoutManager.spanCount
        }else if(layoutManager is LinearLayoutManager){
            if(layoutManager.orientation == LinearLayoutManager.VERTICAL) 1
            else parent.childCount
        } else{
            -1
        }
    }

}