package com.example.asynctaskexample.tasks

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.asynctaskexample.BROADCAST_ACTION_BTN_ENABLE
import com.example.asynctaskexample.EXTRA_RESULT_BUTTON
import com.example.asynctaskexample.activities.MainActivity
import com.example.asynctaskexample.interfaces.CancelAllTasks
import com.example.asynctaskexample.models.App

class CounterTask(
    private val objectWriter: WriteMessageTask,
    private val listenerForFragment: CancelAllTasks,
) : AsyncTask<Void?, Int?, Void?>() {
    companion object {
        const val FINAL_NUMBER = 10
        const val START_NUMBER = 1
    }

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

            sendBroadcastButton()
        }

        sleepThread(5000)
    }

    private fun sendDigit(number: Int) {
        objectWriter.appendMessage(number.toString())
    }

    private fun sleepThread(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (ex: InterruptedException) {
            println(ex)
        }
    }

    private fun sendBroadcastButton() {
        val intent = Intent(BROADCAST_ACTION_BTN_ENABLE)
        intent.putExtra(EXTRA_RESULT_BUTTON, true)
        LocalBroadcastManager.getInstance(App.getInstanceApp()).sendBroadcast(intent)
    }
}