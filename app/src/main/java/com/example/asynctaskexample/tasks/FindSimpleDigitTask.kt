package com.example.asynctaskexample.tasks

import android.os.AsyncTask
import com.example.asynctaskexample.databinding.ActivityMainBinding

class FindSimpleDigitTask(
    private val objectWriter: WriteMessageTask?,
) : AsyncTask<Void?, Int?, Void?>() {
    companion object {
        const val MIN_NUMBER = 1
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            for (i in MIN_NUMBER..Int.MAX_VALUE) {

                if (isCancelled) {
                    return null
                }

                findSimpleDigit(i)
            }

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    private fun findSimpleDigit(currentDigit: Int) {
        var isSimple = true

        for (j in 2 until currentDigit) {
            if (currentDigit.toDouble() % j.toDouble() == 0.0) {
                isSimple = false
                break
            }
        }

        if (isSimple) {
            sendSimpleDigit(currentDigit)
            wakeUpMessageThread()
        }

        sleepThread(1000)
    }

    private fun sendSimpleDigit(number: Int) {
        objectWriter?.appendMessage(number.toString())
    }

    private fun wakeUpMessageThread() {
        val asyncTask4 = objectWriter?.let { SendCustomMessageTask(it) }
        asyncTask4?.executeOnExecutor(
            AsyncTask.THREAD_POOL_EXECUTOR
        )
    }

    private fun sleepThread(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (ex: InterruptedException) {
            println(ex)
        }
    }
}