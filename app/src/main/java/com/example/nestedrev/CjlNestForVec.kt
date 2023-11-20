package com.example.nestedrev

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import java.lang.reflect.Field

class CjlNestForVec @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    private var mScrollerYObj: Any? = null
    private var mCurrVelocity: Field? = null

    init {
        try {
            val mOverScroller = NestedScrollView::class.java.getDeclaredField("mScroller")
            mOverScroller.isAccessible = true
            val obj2 = mOverScroller[this]

            val mScrollerY = obj2.javaClass.getDeclaredField("mScrollerY")
            mScrollerY.isAccessible = true
            mScrollerYObj = mScrollerY[obj2]
            mCurrVelocity = mScrollerYObj!!.javaClass.getDeclaredField("mCurrVelocity")
            mCurrVelocity?.isAccessible = true

        } catch (unused: Throwable) {
        }
    }

    fun getVelocityValue(): Float {
        val res = ((mCurrVelocity?.get(mScrollerYObj) as? Float) ?: 0F)
        return res
    }
}