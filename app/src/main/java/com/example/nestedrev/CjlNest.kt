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
import java.lang.Math.abs
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
    var fra: View? = null
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

    // 没有返回值, 只是给与一个回调表示已经接收到滚动
    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        Log.e("cjlAccept", "${getName(target)}")
        super.onNestedScrollAccepted(child, target, axes)
    }

    // 标记手指按下去的时候是否正在滚动
    val downWhenScrolling = AtomicBoolean(false)

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        // 记录手指按下去的时候是否正在滚动
        Log.e("cjlcjl", "${rev1!!.scrollState != 0 || rev2!!.scrollState != 0}, ${rev1!!.scrollState} - ${rev2!!.scrollState}")
        downWhenScrolling.set(rev1!!.scrollState != 0 || rev2!!.scrollState != 0)

        // 如果是touch类型的话, 就需要手动停止. 防止两个RecyclerView同时在fling[这个很关键]
        if (type == ViewCompat.TYPE_TOUCH) {
            if (target == rev1) {
                rev2!!.stopScroll()
            } else {
                rev1!!.stopScroll()
            }
        }
        val temp = super.onStartNestedScroll(child, target, axes, type)
        cloge("start: ${getName(target)} type: ${type}")
        return temp
    }

    // consumed表示父组件先消费多少, x, y, 剩余的会转给子组件
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (type == 1) {
            cloge("fling pre scroll ${dy}")
        } else {

        }
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
        cloge("scroll: ${getName(target)} ${dy} ${consumed[1]}")
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        cloge("dy: ${dyUnconsumed}")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun onStopNestedScroll(target: View) {
        super.onStopNestedScroll(target)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        if (type == 1) {
            if (target == rev1) {
                if (rev2!!.canScrollVertically(revHolder1!!.getVelocityValue().toInt())) {
                    rev2!!.fling(0, revHolder1!!.getVelocityValue().toInt())
                }
            }
            if (target == rev2) {
                if (rev1!!.canScrollVertically(revHolder2!!.getVelocityValue().toInt())) {
                    rev1!!.fling(0, revHolder2!!.getVelocityValue().toInt())
                }
            }
        }
        super.onStopNestedScroll(target, type)
    }

    // 子组件消费不完的会再给父组件
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
        // 如果是,fling 那么需要考虑把它发给其他的孩子节点, 需要手动做二次消费
        if (false && type == 1 && abs(consumed[1]) < abs(dyUnconsumed)) {
            val leftDy = dyUnconsumed - consumed[1]
            Log.e("fenfa", "${dyUnconsumed - consumed[1]}")
//            consumed[1] = dyUnconsumed
            if (showUpper(leftDy)) {
                /**
                 * dy < 0
                 * 手指向下滑动, 展示上边的元素
                 * 1. rev2滚动
                 * 2. parent滚动
                 * 3. rev1滚动
                 */
//                if (target == rev2) {
//                    rev1!!.fling(0, revHolder2!!.getVelocityValue().toInt())
//                }
//                rev1!!.scrollBy(0, leftDy)
//                if (target == rev1 && canScrollVertically(dy)) {
//                    consumed[1] += max(-scrollY, dy)
//                    scrollBy(0, consumed[1])
//                }
            } else {
                /**
                 * dy > 0
                 * 手指向上滑动, 展示下边的元素
                 * 1. rev1滚动
                 * 2. parent滚动
                 * 3. rev2滚动
                 */
//                rev2!!.scrollBy(0, leftDy)

                // 如果是滑动rev2,那么优先让外层消费
//                if (target == rev2 && canScrollVertically(dy)) {
//                    consumed[1] += min((llv!!.height - height) - scrollY, dy)
//                    scrollBy(0, consumed[1])
//                }
            }
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
//        return super.onNestedPreFling(target, velocityX, velocityY)
//        return !target.canScrollVertically(velocityY.toInt())

    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        Log.e("cjl", "vy: ${velocityY}")
        Log.e("cjlfling", "target: ${getName(target)} vy: ${velocityY}: consumed: $consumed")
//        cloge("target: ${getName(target)} vy: ${velocityY}: consumed: $consumed")
//        if (!consumed) {
////            dispatchNestedFling(0f, velocityY, true)
//            fling(-velocityY.toInt())
//            return true
//        }
        return false
//        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }

    override fun stopNestedScroll() {
        super.stopNestedScroll()
    }

    override fun stopNestedScroll(type: Int) {
        if (type == 1) {
            Log.e("cjlStop", "type: ${type}")
        }
//        Log.e("cjlcjl", "false")
//        flag.set(false)
        super.stopNestedScroll(type)
    }
}

fun cloge(msg: String) {
    Log.e("caijialun", msg)
}