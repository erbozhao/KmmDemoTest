package com.example.kmmdemotest.handler

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.getScopeId
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * @Author: onuszhao
 * @Date: 2025-04-01 19:37
 * @Description:
 */
class MessageHandler(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val taskScope = CoroutineScope(dispatcher)

    private val taskQueues = ArrayDeque<suspend () -> Unit>()
    private val mutex = Mutex()

    private suspend fun addQueue(item: suspend () -> Unit) {
        mutex.withLock {
            taskQueues.addLast(item)
        }
    }

    private suspend fun takeQueue(): (suspend () -> Unit)? {
        return mutex.withLock {
            taskQueues.removeFirstOrNull()
        }
    }

    private suspend fun removeQueue(item: suspend () -> Unit): Boolean {
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

    fun post(block: suspend () -> Unit) {
        taskScope.launch {
            addQueue(block)
            execTaskIfNeed()
        }
    }

    fun postDelayed(block: suspend () -> Unit, delayMillis: Long) {
        println("postDelayed  ${block.getScopeId()}")
        taskScope.launch {
            delay(delayMillis)
            addQueue(block)
            execTaskIfNeed()
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    private val taskRunning = AtomicBoolean(false)

    @OptIn(ExperimentalAtomicApi::class)
    private suspend fun execTaskIfNeed() {
        while (!isEmptyQueue() && !taskRunning.load()) {
            taskRunning.store(true)
            val task = takeQueue()
            task?.invoke()
            taskRunning.store(false)
        }
    }
}
