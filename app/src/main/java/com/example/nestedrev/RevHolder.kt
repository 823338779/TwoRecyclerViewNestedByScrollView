package com.example.nestedrev

import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.nestedrev.CartNestedHelper.getCjlState
import java.lang.reflect.Field

class RevHolder(val nestView: CjlNest, val rev: RecyclerView, val name: String) {
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
        } catch (unused: Throwable) { }

        rev.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                Log.e("change", "${name}: ${getCjlState(newState)}")
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.e("scrollccc", "${name}: ${dy}")
                super.onScrolled(recyclerView, dx, dy)
            }
        })


        rev.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                // 如果它自己没在滚动、并且是从我这里抬起来的
                if (e.action == MotionEvent.ACTION_UP) {
                    Log.e("cjlcjl", "${nestView.downWhenScrolling.get()} - ${rv.scrollState}")
                    // 如果手指按下去时候正在滚动，并且当前rev它没被划动，那么直接拦截掉这个up事件，不让rev消费到
                    if (nestView.downWhenScrolling.get() && rv.scrollState == 0) {
                        return true
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                Log.e("cjlcjl", "拦截成功！")
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }

        })
    }

    fun getVelocityValue(): Float {
        val res = ((mCurrVelocity?.get(mScrollerYObj) as? Float) ?: 0F) * 0.7f
        return res
    }

    fun itSelf(v: View): Boolean {
        return rev == v
    }
}