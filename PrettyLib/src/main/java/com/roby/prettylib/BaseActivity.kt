package com.roby.prettylib

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Roby on 2020/4/25 11:48
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    protected fun showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }
}