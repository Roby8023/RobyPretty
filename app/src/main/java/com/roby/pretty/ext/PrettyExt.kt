package com.roby.pretty.ext

import android.content.res.Resources

/**
 * Created by Roby on 2020/5/30 12:31
 */

fun Number.halfOf(): Number {
    return when (this) {
        is Float -> this / 2
        is Int -> this / 2
        is Double -> this / 2
        else -> this.toFloat() / 2
    }
}

fun Int.dpToPx(): Int {
    return Math.round(this * Resources.getSystem().displayMetrics.density)
}