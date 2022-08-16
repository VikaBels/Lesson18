package com.example.asynctaskexample.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.asynctaskexample.R
import com.example.asynctaskexample.activities.MainActivity
import com.example.asynctaskexample.databinding.FragmentResultAllTasksBinding
import com.example.asynctaskexample.interfaces.CancelAllTasks
import com.example.asynctaskexample.tasks.CounterTask
import com.example.asynctaskexample.tasks.FindSimpleDigitTask
import com.example.asynctaskexample.tasks.WriteMessageTask

class ResultAllTasksFragment : Fragment(), CancelAllTasks {
    private var bindingFragment: FragmentResultAllTasksBinding? = null

    private var asyncTask1: WriteMessageTask? = null
    private var asyncTask2: CounterTask? = null
    private var asyncTask3: FindSimpleDigitTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFragment = FragmentResultAllTasksBinding.inflate(layoutInflater)

        return bindingFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListenerBtnStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingFragment = null
    }

    override fun cancel() {
        asyncTask1?.cancel(false)
        asyncTask2?.cancel(false)
        asyncTask3?.cancel(false)
    }

    private fun clickListenerBtnStart() {
        bindingFragment?.buttonStart?.setOnClickListener {
            setBtnStartDisable()
            startAllAsyncTasks()
        }
    }

    private fun setBtnStartDisable() {
        bindingFragment?.buttonStart?.apply {
            isEnabled = false
            setTextColor(ContextCompat.getColor(context, R.color.grey))
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
        }
    }

    private fun startAllAsyncTasks() {
        asyncTask1 = WriteMessageTask(bindingFragment)
        startAsyncTask(null, asyncTask1)

        asyncTask2 = CounterTask(bindingFragment, asyncTask1, this)
        startAsyncTask(asyncTask2, null)

        asyncTask3 = FindSimpleDigitTask(asyncTask1)
        startAsyncTask(asyncTask3, null)
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
}