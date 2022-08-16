package com.example.asynctaskexample.tasks

import android.os.AsyncTask
import com.example.asynctaskexample.databinding.FragmentResultAllTasksBinding

class WriteMessageTask(
    private val bindingFragment: FragmentResultAllTasksBinding?,
) : AsyncTask<Void?, String?, Void?>() {

    private val listMessage = ArrayList<String>()

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        bindingFragment?.container?.text = values[0]
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            if (isCancelled) {
                return null
            }

            while (true) {
                writeMessage()
                sleepThread(100)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

//    override fun onPostExecute(result: Void?) {
//        super.onPostExecute(result)
//        LocalBroadcastManager
//            .getInstance(App.getInstance())
//            .sendBroadcast(Intent(BROADCAST_ACTION)
//                .putExtra(EXTRA_RESULT, "DONE")
//        )
//
//        if(getContext()!=null){
//            println("setResultText('Done')")
//        }
//    }

    private fun writeMessage() {
        val stringListMessage: String
        val finalText = StringBuilder()

        synchronized(this) {
            stringListMessage = listMessage.joinToString(
                separator = " "
            )

            listMessage.clear()
        }

        finalText.apply {
            append(bindingFragment?.container?.text)
            append(stringListMessage)
        }

        publishProgress(finalText.toString())
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