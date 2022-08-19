package com.example.asynctaskexample.tasks

import android.os.AsyncTask

class SendCustomMessageTask(
    private val objectWriter: WriteMessageTask
) : AsyncTask<Void?, String?, Void?>() {
    companion object {
        const val OUTPUT_TEXT = "Yup!"
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            if (isCancelled) {
                return null
            }

            objectWriter.appendMessage(OUTPUT_TEXT)

            sleepThread(100)

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    private fun sleepThread(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (ex: InterruptedException) {
            println(ex)
        }
    }
}