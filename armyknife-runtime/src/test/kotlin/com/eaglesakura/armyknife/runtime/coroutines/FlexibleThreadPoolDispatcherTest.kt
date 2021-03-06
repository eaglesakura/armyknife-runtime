package com.eaglesakura.armyknife.runtime.coroutines

import com.eaglesakura.armyknife.runtime.Random
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Ignore
import org.junit.Test

class FlexibleThreadPoolDispatcherTest {

    @Test
    fun launch_success() = runBlocking {
        val dispatcher = FlexibleThreadPoolDispatcher.newDispatcher(5, 10, TimeUnit.MILLISECONDS)

        withContext(dispatcher) {
            delay(10)
            println("dispatch success")
        }
    }

    @Test
    fun launch_success_with_current() = runBlocking {
        val dispatcher = FlexibleThreadPoolDispatcher.newDispatcher(5, 10, TimeUnit.MILLISECONDS)

        withContext(coroutineContext + dispatcher) {
            delay(10)
            println("dispatch success")
        }
    }

    @Ignore
    @Test
    fun launch_auto_scale() = runBlocking {
        val dispatcher = FlexibleThreadPoolDispatcher.newDispatcher(5, 10, TimeUnit.MILLISECONDS)

        val loop = 10000
        val channel = Channel<Int>(loop)
        for (i in 0..loop) {
            GlobalScope.launch(dispatcher) {
                delay((Random.int32() % 10 + 1).toLong())
                channel.send(i)
            }
        }

        // Can't Run this test
//        while (!channel.isFull) {
//            delay(1)
//        }
    }
}
