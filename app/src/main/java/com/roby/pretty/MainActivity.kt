package com.roby.pretty

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.roby.prettylib.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : BaseActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showToast("Love U ~ 🍬")

        // GlobalScope.launch 后面的 Dispatchers.Main 就是指定协程在 Android 主线程里运行。
//        val job = GlobalScope.launch(Dispatchers.Main) {
//            val content = fetchData()
//            Log.d("RobyFlag", content)
//        }
////        var job2 = MainScope.launch(Dispatchers.Main) {
////            val content = fetchData()
////            Log.d("RobyFlag", content)
////        }
//
//        Log.e("RobyFlag", "Pretty Roby 🍬")
//
//        Log.e("RobyFlag", ">>> Pretty Roby 🍬")

        CoroutineScope(Dispatchers.Main).launch {

        }


        GlobalScope.launch(Dispatchers.IO) {
            val resultData = fetchData()
            withContext(Dispatchers.Main) {
                // IO 更新UI:  android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
                tvClick.text = resultData
                Toast.makeText(this@MainActivity, resultData, Toast.LENGTH_SHORT).show()
            }
        }

        tvClick.setOnClickListener {
            runBlocking {
                val content = fetchData()
                Log.d("RobyFlag", ">>> $content")
            }
        }

        Log.e("RobyFlag", ">>> Pretty Roby 🍬 love 1")
    }

    /**
     * suspend 方法能够使协程执行暂停，等执行完毕后在返回结果，同时不会阻塞线程。
     * 而且暂停协程里方法的执行，直到方法返回结果，这样也不用写 Callback 来取结果，可以使用同步的方式来写异步代码，真是漂亮啊。
     */
    private suspend fun fetchData(): String {
        delay(4000)
        return "Roby 8023"
    }


    override fun onDestroy() {
        // cancel coroutine
        cancel()
        super.onDestroy()
    }
}
