package com.example.nestedrev

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView.OnItemClickListener
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


//        rev.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
//            var mGestureDetector = GestureDetector(rev.context, object : SimpleOnGestureListener() {
//                override fun onSingleTapUp(e: MotionEvent): Boolean {
//                    return true
//                }
//
//                override fun onLongPress(e: MotionEvent) {
//                }
//            })
//
//            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
//                if (mGestureDetector.onTouchEvent(e)) {
//                    return true
//                }
//                return false
//            }
//
//            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
//            }
//
//            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//            }
//        })
    }

    fun getVelocityValue(): Float {
        val res = ((mCurrVelocity?.get(mScrollerYObj) as? Float) ?: 0F) * 0.7f
        return res
    }

    fun itSelf(v: View): Boolean {
        return rev == v
    }
}