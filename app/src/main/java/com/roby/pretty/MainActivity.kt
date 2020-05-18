package com.roby.pretty

import android.os.Bundle
import com.roby.prettylib.BaseActivity
import com.roby.prettylib.util.DebugLog

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showToast("Love U ~ 🍬")
        DebugLog.e("Pretty Roby 🍬")

        testThreadJoinFunction()
    }

    private fun testThreadJoinFunction() {

    }
}
