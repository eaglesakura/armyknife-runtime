package com.eaglesakura.armyknife.runtime

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Object with singleton pattern helper.
 *
 * Don't use always this class.
 * You should consideration the architecture pattern for singleton.
 * Thinking "Lazy" or "LazySingleton" or "object Class" or such classes.
 *
 * e.g.)
 * val instance : LazySingleton<Foo>()
 *
 * fun getFoo() : Foo {
 *      instance.get {
 *          // this block call once.
 *          Foo()
 *      }
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/armyknife-runtime
 * @link https://github.com/eaglesakura/lazysingleton
 */
@Deprecated("https://github.com/eaglesakura/lazysingleton")
class LazySingleton<T> {
    private var instance: T? = null

    private val lock = ReentrantLock()

    fun get(factory: () -> T): T {
        if (instance == null) {
            lock.withLock {
                if (instance == null) {
                    instance = factory()
                }
            }
        }
        return instance!!
    }
}
