package com.chkan.bestpractices.gestures

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.GestureDetectorCompat
import com.chkan.bestpractices.R
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class GesturesFragment : Fragment() {

    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gestures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*gestureDetector = GestureDetectorCompat(requireActivity(), SwipeGestureListener())
        view.setOnTouchListener { v, event ->
            Log.d("CHKAN", "event: $event")
            gestureDetector.onTouchEvent(event)
            true
        }*/

        view.findViewById<ImageView>(R.id.iv_for_click).setOnClickListener {
            showToast( "Clicked")
        }


        // Detect and consume all events
        SwipeEvents.detect( view, object : SwipeEvents.SwipeCallback {

            override fun onSwipeTop() {
                showToast( "Swiped top")
            }

            override fun onSwipeRight() {
                showToast( "Swiped right")
            }

            override fun onSwipeBottom() {
                showToast( "Swiped bottom")
            }

            override fun onSwipeLeft() {
                showToast( "Swiped left")
            }
        })

        // Detect and consume specific events
        // {Available methods} - detectTop, detectRight, detectBottom, detectLeft
        /*SwipeEvents.detectTop(view, object : SwipeEvents.SwipeSingleCallback {

            override fun onSwipe() {
                showToast("Swiped - detectTop")
            }
        })*/
    }

    fun showToast(message :String ){
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

}

private class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {

    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100

    override fun onDown(event: MotionEvent): Boolean {
        return false
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        try {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        Log.d("CHKAN", "onFling: Left to Right swipe gesture")
                    }
                    else {
                        Log.d("CHKAN", "onFling: Right to Left swipe gesture")
                    }
                }
            }
        }
        catch (exception: Exception) {
            Log.d("CHKAN", "onFling: ERROR ${exception.printStackTrace()}")
        }
        return false
    }
}