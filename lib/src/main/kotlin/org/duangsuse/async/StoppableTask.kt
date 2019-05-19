package org.duangsuse.async

interface StoppableTask {
    fun stop()
    fun isStopped(): Boolean
}