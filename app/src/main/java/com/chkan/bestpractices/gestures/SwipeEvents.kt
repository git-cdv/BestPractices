package com.chkan.bestpractices.gestures

import android.util.Log
import android.view.MotionEvent
import android.view.View


class SwipeEvents : View.OnTouchListener {
    private var swipeCallback: SwipeCallback? = null
    private var swipeSingleCallback: SwipeSingleCallback? = null
    private var detectSwipeDirection: SwipeDirection? = null
    var x1 = 0f
    var x2 = 0f
    var y1 = 0f
    var y2 = 0f
    var view: View? = null

    private fun detect() {
        view?.setOnTouchListener(this)
    }

    private fun detectSingle(direction: SwipeDirection) {
        detectSwipeDirection = direction
        view?.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.d("CHKAN", "event: $event")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                y2 = event.y
                var direction: SwipeDirection? = null
                val xDiff = x2 - x1
                val yDiff = y2 - y1
                direction = if (Math.abs(xDiff) > Math.abs(yDiff)) {
                    if (x1 < x2) {
                        SwipeDirection.RIGHT
                    } else {
                        SwipeDirection.LEFT
                    }
                } else {
                    if (y1 > y2) {
                        SwipeDirection.TOP
                    } else {
                        SwipeDirection.BOTTOM
                    }
                }

                // Only trigger the requested event only if there
                if (detectSwipeDirection != null && swipeSingleCallback != null) {
                    if (direction == detectSwipeDirection) {
                        swipeSingleCallback?.onSwipe()
                    }
                } else {
                    if (direction == SwipeDirection.RIGHT) {
                        swipeCallback?.onSwipeRight()
                    }
                    if (direction == SwipeDirection.LEFT) {
                        swipeCallback?.onSwipeLeft()
                    }
                    if (direction == SwipeDirection.TOP) {
                        swipeCallback?.onSwipeTop()
                    }
                    if (direction == SwipeDirection.BOTTOM) {
                        swipeCallback?.onSwipeBottom()
                    }
                }
            }
        }
        return true
    }

    enum class SwipeDirection {
        TOP, RIGHT, BOTTOM, LEFT
    }

    interface SwipeCallback {
        fun onSwipeTop()
        fun onSwipeRight()
        fun onSwipeBottom()
        fun onSwipeLeft()
    }

    interface SwipeSingleCallback {
        fun onSwipe()
    }

    companion object {
        private fun newInstance(): SwipeEvents {
            return SwipeEvents()
        }

        fun detect(view: View?, swipeCallback: SwipeCallback?) {
            val evt = newInstance()
            evt.swipeCallback = swipeCallback
            evt.view = view
            evt.detect()
        }

        fun detectTop(view: View?, swipeSingleCallback: SwipeSingleCallback?) {
            val evt = newInstance()
            evt.swipeSingleCallback = swipeSingleCallback
            evt.view = view
            evt.detectSingle(SwipeDirection.TOP)
        }

        fun detectRight(view: View?, swipeSingleCallback: SwipeSingleCallback?) {
            val evt = newInstance()
            evt.swipeSingleCallback = swipeSingleCallback
            evt.view = view
            evt.detectSingle(SwipeDirection.RIGHT)
        }

        fun detectBottom(view: View?, swipeSingleCallback: SwipeSingleCallback?) {
            val evt = newInstance()
            evt.swipeSingleCallback = swipeSingleCallback
            evt.view = view
            evt.detectSingle(SwipeDirection.BOTTOM)
        }

        fun detectLeft(view: View?, swipeSingleCallback: SwipeSingleCallback?) {
            val evt = newInstance()
            evt.swipeSingleCallback = swipeSingleCallback
            evt.view = view
            evt.detectSingle(SwipeDirection.LEFT)
        }
    }
}