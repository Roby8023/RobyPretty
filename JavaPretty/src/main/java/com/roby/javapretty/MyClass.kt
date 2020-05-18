package com.roby.javapretty

public class MyClass {

}

fun main() {
//    mainFun1()
    mainFun2()
}

fun mainFun2() {
    val instance: ThreadJoinTest = ThreadJoinTest("Roby")
    val t1 = Thread(instance)
    val t2 = Thread(instance)
    t2.start()
    t1.start()
    t1.join()
    t2.join()
}

private fun mainFun1() {
    val t1 = ThreadJoinTest("--- 小明")
    val t2 = ThreadJoinTest("=== 小东")
    t2.start()
    t1.start()

    /**
     * join的意思是使得放弃当前线程的执行，并返回对应的线程，例如下面代码的意思就是：
     * 程序在main线程中调用t1线程的join方法，则main线程放弃cpu控制权，并返回t1线程继续执行直到线程t1执行完毕
     * 所以结果是t1线程执行完后，才到主线程执行，相当于在main线程中同步t1线程，t1执行完了，main线程才有执行的机会
     */
    t1.join()
    t2.join()
}

class ThreadJoinTest(name: String?) : Thread(name) {
    //共享资源(临界资源)
    var i = 0

    /**
     * synchronized 修饰实例方法
     */
    @Synchronized
    fun increase() {
        i++
        println("$name - $i")
    }

    override fun run() {
        for (i in 0..10) {
            increase()
//            sleep(1000)
//            println("$name:$i")
        }
    }
}