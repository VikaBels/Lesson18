package com.example.asynctaskexample.tasks

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.asynctaskexample.R
import com.example.asynctaskexample.databinding.FragmentResultAllTasksBinding
import com.example.asynctaskexample.interfaces.CancelAllTasks

class CounterTask(
    private val bindingFragment: FragmentResultAllTasksBinding?,
    private val objectWriter: WriteMessageTask?,
    private val listenerForFragment: CancelAllTasks,

) : AsyncTask<Void?, Int?, Void?>() {
    companion object {
        const val FINAL_NUMBER = 10
        const val START_NUMBER = 1
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            for (i in START_NUMBER..FINAL_NUMBER) {
                if (isCancelled) {
                    return null
                }

                counterOneTen(i)
            }

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }



    private fun counterOneTen(currentDigit: Int) {
        sendDigit(currentDigit)

        if (currentDigit == FINAL_NUMBER) {
            sleepThread(100)

            listenerForFragment.cancel()

            handler.post {
                setButtonEnable()
            }
        }

        sleepThread(5000)
    }

    private fun sendDigit(number: Int) {
        objectWriter?.appendMessage(number.toString())
    }

    private fun sleepThread(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (ex: InterruptedException) {
            println(ex)
        }
    }

    private fun setButtonEnable() {
        bindingFragment?.buttonStart?.apply {
            isEnabled = true
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
        }
    }
}