package org.duangsuse.async

class StoppableTaskGroup(private val tasks: Set<StoppableTask> = mutableSetOf())
    : Collection<StoppableTask> by tasks, StoppableTask {
    override fun isStopped(): Boolean = tasks.all(StoppableTask::isStopped)
    override fun stop() = tasks.forEach(StoppableTask::stop)
}