package com.example.nestedrev

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.nestedrev.CartNestedHelper.showUpper
import java.lang.Integer.max
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min

class CjlNest @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private var rev1: RecyclerView? = null
    private var rev2: RecyclerView? = null
    private var revHolder1: RevHolder? = null
    private var revHolder2: RevHolder? = null
    var llv: LinearLayout? = null

    private fun getName(o: View): String {
        if (o == rev1) {
            return "rev1"
        } else if (o == rev2) {
            return "rev2"
        }
        return "NO"
    }

    fun setUpRev(r1: RecyclerView, r2: RecyclerView) {
        rev1 = r1
        rev2 = r2
        revHolder1 = RevHolder(this, r1, getName(r1))
        revHolder2 = RevHolder(this, r2, getName(r2))
    }

    // 标记手指按下去的时候是否正在滚动
    val downWhenScrolling = AtomicBoolean(false)


    override fun startNestedScroll(axes: Int): Boolean {
        return super.startNestedScroll(axes)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return super.startNestedScroll(axes, type)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        if (type == 0) {
            downWhenScrolling.set(rev1!!.scrollState != 0 || rev2!!.scrollState != 0)
        }
        // 如果是touch类型的话, 就需要手动停止. 防止两个RecyclerView同时在fling[这个很关键]
        if (type == ViewCompat.TYPE_TOUCH) {
            if (target == rev1) {
                rev2!!.stopScroll()
            } else {
                rev1!!.stopScroll()
            }
        }
        super.onNestedScrollAccepted(child, target, axes, type)
    }

    // consumed表示父组件先消费多少, x, y, 剩余的会转给子组件
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        cloge("${getName(target)}, ${dy}, ${rev1?.scrollState}, ${rev2?.scrollState}")
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        if (showUpper(dy)) {
            /**
             * dy < 0
             * 手指向下滑动, 展示上边的元素
             * 1. rev2滚动
             * 2. parent滚动
             * 3. rev1滚动
             */
            if (target == rev1 && canScrollVertically(dy)) {
                consumed[1] += max(-scrollY, dy)
                scrollBy(0, consumed[1])
            }
        } else {
            /**
             * dy > 0
             * 手指向上滑动, 展示下边的元素
             * 1. rev1滚动
             * 2. parent滚动
             * 3. rev2滚动
             */
            // 如果是滑动rev2,那么优先让外层消费
            if (target == rev2 && canScrollVertically(dy)) {
                consumed[1] += min((llv!!.height - height) - scrollY, dy)
                scrollBy(0, consumed[1])
            }
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        val leftDy = dyUnconsumed - consumed[1]
        if (target == rev1 && leftDy > 0 && rev2?.canScrollVertically(leftDy) == true) {
            rev2?.scrollBy(0, leftDy)
            consumed[1] += leftDy
        } else if (target == rev2 && leftDy < 0 && rev1?.canScrollVertically(leftDy) == true) {
            rev1?.scrollBy(0, leftDy)
            consumed[1] += leftDy
        }

    }
}

fun cloge(msg: String) {
    Log.e("caijialun", msg)
}