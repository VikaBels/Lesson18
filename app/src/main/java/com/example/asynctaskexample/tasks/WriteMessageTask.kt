package com.example.asynctaskexample.tasks

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.asynctaskexample.BROADCAST_ACTION_SEND_TEXT
import com.example.asynctaskexample.EXTRA_RESULT_TEXT
import com.example.asynctaskexample.models.App.Companion.getInstanceApp
import com.example.asynctaskexample.models.App.Companion.getSubscribe

class WriteMessageTask : AsyncTask<Void?, String?, Void?>() {

    private val listMessage = mutableListOf<String>()

    private var previouslySigned = true

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        sendBroadcast(values.joinToString())
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            while (!isCancelled) {
                writeMessage()
                sleepThread(100)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    private fun clearBuffer() {
        listMessage.clear()
    }

    private fun addValueInBuffer(value: String) {
        if (value.isNotEmpty()) {
            listMessage.add(value)
        }
    }

    private fun sendBroadcast(value: String) {
        val intent = Intent(BROADCAST_ACTION_SEND_TEXT)
        intent.putExtra(EXTRA_RESULT_TEXT, value)
        LocalBroadcastManager.getInstance(getInstanceApp()).sendBroadcastSync(intent)
    }

    private fun writeMessage() {
        val stringListMessage: String

        synchronized(this) {
            stringListMessage = listMessage.joinWithSeparator()

            listMessage.clear()
        }

        sendMessage(stringListMessage)
    }

    private fun sendMessage(stringListMessage: String) {
        if (getSubscribe()) {

            if (!previouslySigned) {
                publishProgress(listMessage.joinWithSeparator())
                clearBuffer()

                previouslySigned = true
            }
            publishProgress(stringListMessage)

        } else {
            addValueInBuffer(stringListMessage)
            previouslySigned = false
        }
    }

    private fun List<String>.joinWithSeparator(): String {
        return joinToString(separator = " ")
    }

    private fun sleepThread(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (ex: InterruptedException) {
            println(ex)
        }
    }

    fun appendMessage(text: String) {
        synchronized(this) {
            listMessage.add("$text,")
        }
    }
}




