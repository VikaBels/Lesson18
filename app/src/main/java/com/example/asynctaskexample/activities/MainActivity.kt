package com.example.asynctaskexample.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.asynctaskexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var bindingMain: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingMain = null
    }

//    override fun cancel() {
//        asyncTask1?.cancel(false)
//        asyncTask2?.cancel(false)
//        asyncTask3?.cancel(false)
//    }
//
//    private fun clickListenerBtnStart() {
//        bindingMain?.buttonStart?.setOnClickListener {
//            setBtnStartDisable()
//            startAllAsyncTasks()
//        }
//    }
//
//    private fun setBtnStartDisable() {
//        bindingMain?.buttonStart?.apply {
//            isEnabled = false
//            setTextColor(ContextCompat.getColor(context, R.color.grey))
//            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
//        }
//    }
//
//    private fun startAllAsyncTasks() {
//        asyncTask1 = WriteMessageTask(bindingMain)
//        startAsyncTask(null, asyncTask1)
//
//        asyncTask2 = CounterTask(bindingMain, asyncTask1, this)
//        startAsyncTask(asyncTask2, null)
//
//        asyncTask3 = FindSimpleDigitTask(asyncTask1)
//        startAsyncTask(asyncTask3, null)
//    }
//
//    private fun startAsyncTask(
//        taskInt: AsyncTask<Void?, Int?, Void?>?,
//        taskString: AsyncTask<Void?, String?, Void?>?
//    ) {
//        if (taskInt != null) {
//            taskInt.executeOnExecutor(
//                AsyncTask.THREAD_POOL_EXECUTOR
//            )
//        } else {
//            taskString?.executeOnExecutor(
//                AsyncTask.THREAD_POOL_EXECUTOR
//            )
//        }
//    }
}
/*
    <ProgressBar
        android:id="@+id/progressBar5"
        style="?android:attr/progressBarStyleHorizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="100"
        android:progress="0" />
    <TextView
        android:id="@+id/info_text5"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"/>

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

asyncTask4 = SendCustomMessageTask(bindingMain, asyncTask1)
startAsyncTask(null, asyncTask4)


inner class MyAsyncTask5(
    private val mProgressBar: ProgressBar?,
    private val bindingMain: ActivityMainBinding?
) : AsyncTask<Void?, Int?, Void?>() {

    override fun onPreExecute() {
        super.onPreExecute()

        bindingMain?.infoText3?.text = "Запуск 5 потока"
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        mProgressBar?.progress = values[0]!!
        bindingMain?.infoText5?.text = "Число: " + values[0]
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        bindingMain?.infoText5?.text = "Завершение 5 потока"
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            var counter = 0
            for (i in 0..99) {
                sleepThread()
                publishProgress(++counter)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    private fun sleepThread() {
        SystemClock.sleep(100)
    }
}
*/