package com.example.nested

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Field

class RevHolder(val rev: RecyclerView) {
    private var mScrollerYObj: Any? = null
    private var mCurrVelocity: Field? = null

    init {
        try {
            // 用反射获取 flinger
            val mViewFlinger = RecyclerView::class.java.getDeclaredField("mViewFlinger")
            mViewFlinger.isAccessible = true
            val obj = mViewFlinger[rev]
            // 用反射获取 flinger中的scroller
            val mOverScroller = obj.javaClass.getDeclaredField("mOverScroller")
            mOverScroller.isAccessible = true
            val obj2 = mOverScroller[obj]

            val mScrollerY = obj2.javaClass.getDeclaredField("mScrollerY")
            mScrollerY.isAccessible = true
            mScrollerYObj = mScrollerY[obj2]
            mCurrVelocity = mScrollerYObj!!.javaClass.getDeclaredField("mCurrVelocity")
            mCurrVelocity?.isAccessible = true
        } catch (unused: Throwable) {
        }
    }

    fun getVelocityValue(): Float {
        return ((mCurrVelocity?.get(mScrollerYObj) as? Float) ?: 0F) * 0.8f
    }

    fun itSelf(v: View): Boolean {
        return rev == v
    }
}