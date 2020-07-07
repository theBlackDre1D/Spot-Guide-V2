package com.g3.spot_guide.extensions

import android.os.SystemClock
import android.view.View

private const val CLICK_DELAY: Long = 750

fun View.onClick(onClick: (View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime < CLICK_DELAY) {
                return
            } else {
                onClick(v)
            }

            this.lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}