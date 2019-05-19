package org.duangsuse.async

@FunctionalInterface
interface Callback<T, R> {
    fun handleResult(res: TaskResult<out T>): R
    fun handleTaskStart() {}
}