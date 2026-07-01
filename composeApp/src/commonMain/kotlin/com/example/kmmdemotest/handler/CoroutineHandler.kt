package com.example.kmmdemotest.handler

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * @author onuszhao
 * @since 2025/10/27
 * @description
 */

class CoroutineHandler(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private var channel: Channel<suspend CoroutineScope.() -> Unit>? = null

    // 创建一个 CoroutineScope，使用指定的调度器和 Job
    val handlerScope = CoroutineScope(dispatcher)

    @OptIn(ExperimentalAtomicApi::class)
    private val isRunning = AtomicBoolean(value = false)
    private val lock = Mutex()
    private var job: Job? = null
    fun post(block: suspend CoroutineScope.() -> Unit) {
        startRunningIfNeed()
        channel?.trySend(block)
    }

    fun postDelayed(block: suspend CoroutineScope.() -> Unit, delayMillis: Long) {
        handlerScope.launch {
            delay(delayMillis)
            startRunningIfNeed()
            channel?.trySend(block)
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    private fun startRunningIfNeed() {
        if (isRunning.compareAndSet(false, true)) {
            channel = Channel(Channel.UNLIMITED)
            job = handlerScope.launch {
                // 启动一个消费者协程，按顺序处理任务
                channel?.let {
                    for (task in it) { // 挂起直到有消息可用
                        try {
                            if (job?.isActive == true) {
                                //保证完全序列化
                                lock.withLock {
                                    task() // 执行任务
                                }
                            }
                        } catch (e: Exception) {
                            // 处理异常，避免影响后续任务
                        }
                    }
                }

            }
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    fun cancelAll() {
        if (isRunning.compareAndSet(true, false)) {
            channel?.close()
            channel = null
            job?.cancel()
            job = null
        }
    }
}