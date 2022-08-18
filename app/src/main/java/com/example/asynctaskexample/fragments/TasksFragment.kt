package com.example.asynctaskexample.fragments

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asynctaskexample.interfaces.CancelAllTasks
import com.example.asynctaskexample.tasks.CounterTask
import com.example.asynctaskexample.tasks.FindSimpleDigitTask
import com.example.asynctaskexample.tasks.WriteMessageTask

class TasksFragment : Fragment(),
    CancelAllTasks {

    private var asyncTaskWriter: WriteMessageTask? = null
    private var asyncTaskCounter: CounterTask? = null
    private var asyncTaskSimpleDigit: FindSimpleDigitTask? = null

    private var tasksIsStarted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }

    override fun cancel() {
        tasksIsStarted = false

        asyncTaskWriter?.cancel(false)
        asyncTaskCounter?.cancel(false)
        asyncTaskSimpleDigit?.cancel(false)
    }

    private fun startAsyncTask(
        taskInt: AsyncTask<Void?, Int?, Void?>?,
        taskString: AsyncTask<Void?, String?, Void?>?
    ) {
        if (taskInt != null) {
            taskInt.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR
            )
        } else {
            taskString?.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR
            )
        }
    }

    fun startAllAsyncTasks() {
        tasksIsStarted = true

        asyncTaskWriter = WriteMessageTask()
        startAsyncTask(null, asyncTaskWriter)

        asyncTaskCounter = CounterTask(asyncTaskWriter, this)
        startAsyncTask(asyncTaskCounter, null)

        asyncTaskSimpleDigit = FindSimpleDigitTask(asyncTaskWriter)
        startAsyncTask(asyncTaskSimpleDigit, null)
    }

    fun getIsStarted(): Boolean {
        return tasksIsStarted
    }
}