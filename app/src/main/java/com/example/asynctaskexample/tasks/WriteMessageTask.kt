package com.example.asynctaskexample.tasks

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.asynctaskexample.BROADCAST_ACTION_SEND_TEXT
import com.example.asynctaskexample.EXTRA_RESULT_TEXT
import com.example.asynctaskexample.models.App.Companion.getInstanceApp
import com.example.asynctaskexample.models.App.Companion.isSubscribe

class WriteMessageTask : AsyncTask<Void?, String?, Void?>() {

    private val listMessage = mutableListOf<String>()

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

    private fun sendBroadcast(value: String) {
        val intent = Intent(BROADCAST_ACTION_SEND_TEXT)
        intent.putExtra(EXTRA_RESULT_TEXT, value)
        LocalBroadcastManager.getInstance(getInstanceApp()).sendBroadcastSync(intent)
    }

    private fun writeMessage() {
        var stringListMessage: String? = null

        synchronized(this) {
            if (isSubscribe()) {
                stringListMessage = listMessage.joinWithSeparator()

                listMessage.clear()
            }
        }

        stringListMessage?.let {
            publishProgress(it)
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




