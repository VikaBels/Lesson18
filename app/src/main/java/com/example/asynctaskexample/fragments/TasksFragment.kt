package com.example.asynctaskexample.fragments

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.asynctaskexample.interfaces.CancelAllTasks
import com.example.asynctaskexample.tasks.CounterTask
import com.example.asynctaskexample.tasks.FindSimpleDigitTask
import com.example.asynctaskexample.tasks.WriteMessageTask

class TasksFragment : Fragment(),
    CancelAllTasks {

    private var tasks: List<AsyncTask<*, *, *>>? = null

    private var tasksIsStarted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    override fun cancel() {
        if (tasksIsStarted) {
            tasksIsStarted = false

            tasks?.forEach { task ->
                task.cancel(false)
            }
        }
    }

    private fun startAsyncTask(
        task: AsyncTask<Void?, *, Void?>
    ) {
        task.executeOnExecutor(
            AsyncTask.THREAD_POOL_EXECUTOR
        )
    }

    fun startAllAsyncTasks() {
        if (!tasksIsStarted) {

            tasksIsStarted = true

            val asyncTaskWriter = WriteMessageTask()
            val asyncTaskCounter = CounterTask(asyncTaskWriter, this)
            val asyncTaskSimpleDigits = FindSimpleDigitTask(asyncTaskWriter)

            val tasks = listOf(asyncTaskWriter, asyncTaskCounter, asyncTaskSimpleDigits)

            tasks.forEach { task ->
                startAsyncTask(task)
            }

            this.tasks = tasks
        }
    }

    fun getIsStarted(): Boolean {
        return tasksIsStarted
    }
}