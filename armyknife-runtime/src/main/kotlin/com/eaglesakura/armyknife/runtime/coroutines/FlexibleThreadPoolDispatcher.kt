package com.eaglesakura.armyknife.runtime.coroutines

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher

/**
 * Dispatcher with flexible thread pool.
 *
 * Thread pools are auto scale by ThreadPoolExecutor.
 * When do not using this instance in coroutines, Thread will be shutdown by a Dalvik.
 *
 * e.g.)
 *
 * val dispatcher = FlexibleThreadPoolDispatcher.newDispatcher(4, 1, TimeUnit.SECONDS)  // 4thread, 1seconds auto-scale dispatcher.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/armyknife-runtime
 * @link https://github.com/eaglesakura/flexible-thread-pool-dispatcher
 */
@Deprecated("https://github.com/eaglesakura/flexible-thread-pool-dispatcher")
object FlexibleThreadPoolDispatcher {

    /**
     * Returns new auto-scale coroutine dispatcher.
     */
    fun newDispatcher(
        maxThreads: Int,
        keepAliveTime: Long,
        keepAliveTimeUnit: TimeUnit
    ): CoroutineDispatcher {
        return FlexibleThreadPoolExecutor(
            maxThreads,
            keepAliveTime,
            keepAliveTimeUnit
        ).asCoroutineDispatcher()
    }

    /**
     * for Device input/output dispatcher.
     */
    val IO: CoroutineDispatcher by lazy {
        newDispatcher(
            Runtime.getRuntime().availableProcessors() * 2 + 1,
            5,
            TimeUnit.SECONDS
        )
    }

    /**
     * for Network fetch dispatcher.
     */
    val Network: CoroutineDispatcher by lazy {
        newDispatcher(
            Runtime.getRuntime().availableProcessors() * 2 + 1,
            5,
            TimeUnit.SECONDS
        )
    }
}

private class FlexibleThreadPoolExecutor(
    maxThreads: Int,
    keepAliveTime: Long,
    keepAliveTimeUnit: TimeUnit
) : ThreadPoolExecutor(0, maxThreads, keepAliveTime, keepAliveTimeUnit, LinkedBlockingDeque()) {
    override fun execute(command: Runnable?) {
        try {
            corePoolSize = maximumPoolSize
            super.execute(command)
        } finally {
            corePoolSize = 0
        }
    }
}
