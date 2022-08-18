package com.example.asynctaskexample.tasks

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.asynctaskexample.activities.MainActivity.Companion.BROADCAST_ACTION_SEND_TEXT
import com.example.asynctaskexample.activities.MainActivity.Companion.EXTRA_RESULT_TEXT
import com.example.asynctaskexample.models.App.Companion.getInstanceApp

class WriteMessageTask : AsyncTask<Void?, String?, Void?>() {

    private val listMessage = ArrayList<String>()

    private fun sendMessage(value: String) {
        val intent = Intent(BROADCAST_ACTION_SEND_TEXT)
        intent.putExtra(EXTRA_RESULT_TEXT, value)
        LocalBroadcastManager.getInstance(getInstanceApp()).sendBroadcastSync(intent)
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        sendMessage(values.joinToString())
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

    private fun writeMessage() {
        val stringListMessage: String

        synchronized(this) {
            stringListMessage = listMessage.joinToString(
                separator = " "
            )

            listMessage.clear()
        }

        publishProgress(stringListMessage)
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
            println(text)
        }
    }
}