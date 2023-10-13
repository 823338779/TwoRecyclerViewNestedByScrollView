package com.example.nestedrev

import androidx.recyclerview.widget.RecyclerView

object CartNestedHelper {
    fun showUpper(delta: Int): Boolean {
        return delta < 0
    }

    fun canScrollVerticallyUp(rv: RecyclerView): Boolean {
        return canScrollVerticallyUpDistance(rv) > 0
    }

    fun canScrollVerticallyDown(rv: RecyclerView): Boolean {
        return canScrollVerticallyDownDistance(rv) > 0
    }

    private fun canScrollVerticallyUpDistance(rv: RecyclerView): Int {
        return rv.computeVerticalScrollOffset()
    }

    private fun canScrollVerticallyDownDistance(rv: RecyclerView): Int {
        val offset = rv.computeVerticalScrollOffset()
        val range = rv.computeVerticalScrollRange() - rv.computeVerticalScrollExtent()
        return range - offset
    }
    fun getCjlState(ste: Int): String {
        if (ste == 0) {
            return "IDLE"
        } else if (ste == 1) {
            return "DRAG"
        } else {
            return "FLING"
        }
    }

}