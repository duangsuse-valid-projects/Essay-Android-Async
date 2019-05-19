package org.duangsuse.async

data class TaskResult<T>(val successful: Boolean, val res: T, val except: Throwable?)