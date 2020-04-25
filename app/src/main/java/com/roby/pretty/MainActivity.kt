package com.roby.pretty

import android.os.Bundle
import com.roby.prettylib.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showToast("Love U ~ 🍬")
    }
}
