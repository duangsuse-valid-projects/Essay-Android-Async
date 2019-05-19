package org.duangsuse.async

import android.os.AsyncTask

interface StoppableTask {
    fun isStopped(): Boolean
    fun stop()
}

data class TaskResult<T>(val successful: Boolean, val res: T, val except: Throwable?)

class StoppableTaskGroup(private val tasks: Set<StoppableTask> = mutableSetOf())
    : Collection<StoppableTask> by tasks, StoppableTask {
    override fun isStopped(): Boolean = tasks.all(StoppableTask::isStopped)
    override fun stop() = tasks.forEach(StoppableTask::stop)
}

@FunctionalInterface
interface Callback<T, R> {
    fun handleResult(res: TaskResult<out T>): R
    fun handleTaskStart() {}
}

object Async {
    fun <R> beginTask(f: Function0<R>, then: Callback<R?, Nothing>): StoppableTask {
        val task = object : AsyncTask<Void, Nothing, TaskResult<R?>>() {
            override fun doInBackground(vararg params: Void?): TaskResult<R?> {
                val result: R
                try { result = f() }
                catch (e: Throwable) {
                    return TaskResult(successful = false, res = null, except =  e)
                }
                return TaskResult(successful = true, res = result, except = null)
            }

            override fun onPreExecute() = then.handleTaskStart()
            override fun onPostExecute(result: TaskResult<R?>) = then.handleResult(result)
        }

        val cancelable = object : StoppableTask {
            override fun isStopped(): Boolean = task.isCancelled
            override fun stop() { if (!isStopped()) task.cancel(true) }
        }

        task.execute()
        return cancelable
    }
}
