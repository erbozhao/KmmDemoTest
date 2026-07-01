package com.example.kmmdemotest.handler

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * @Author: onuszhao
 * @Date: 2025-04-01 19:37
 * @Description:
 */
class MessageHandler2(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val taskScope = CoroutineScope(dispatcher)

    private val taskQueues = ArrayDeque<CoroutineRunnable>()
    private val mutex = Mutex()

    private suspend fun addQueue(item: CoroutineRunnable) {
        mutex.withLock {
            taskQueues.addLast(item)
        }
    }

    private suspend fun takeQueue(): CoroutineRunnable? {
        return mutex.withLock {
            taskQueues.removeFirstOrNull()
        }
    }

    private suspend fun removeQueue(item: CoroutineRunnable): Boolean {
        return mutex.withLock {
            taskQueues.remove(item)
        }
    }

    private suspend fun isEmptyQueue(): Boolean {
        return mutex.withLock {
            taskQueues.isEmpty()
        }
    }

    private suspend fun queueSize(): Int {
        return mutex.withLock {
            taskQueues.size
        }
    }

    private val deleteQueue = mutableListOf<CoroutineRunnable>()

    private suspend fun addDeleteQueue(item: CoroutineRunnable) {
        mutex.withLock {
            deleteQueue.add(item)
        }
    }

    private suspend fun removeDeleteQueue(item: CoroutineRunnable): Boolean {
        return mutex.withLock {
            deleteQueue.remove(item)
        }
    }

    fun post(block: CoroutineRunnable) {
        taskScope.launch {
            addQueue(block)
            execTaskIfNeed()
        }
    }

    fun postDelayed(block: CoroutineRunnable, delayMillis: Long) {
        taskScope.launch {
            delay(delayMillis)
            val result = removeDeleteQueue(block)
            if (!result) {
                addQueue(block)
                execTaskIfNeed()
            }
        }
    }

    fun remove(block: CoroutineRunnable) {
        taskScope.launch {
            val result = removeQueue(block)
            if (!result) {
                addDeleteQueue(block)
            }
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    private val taskRunning = AtomicBoolean(false)

    @OptIn(ExperimentalAtomicApi::class)
    private suspend fun execTaskIfNeed() {
        while (!isEmptyQueue() && !taskRunning.load()) {
            taskRunning.store(true)
            val task = takeQueue()
            task?.run()
            taskRunning.store(false)
        }
    }
}
