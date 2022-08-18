package com.example.asynctaskexample.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.asynctaskexample.R
import com.example.asynctaskexample.databinding.ActivityMainBinding
import com.example.asynctaskexample.fragments.TasksFragment
import com.example.asynctaskexample.models.App.Companion.getInstanceApp

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG_TASK_FRAGMENT = "tagForHelperFragment"
        const val EXTRA_RESULT_TEXT = "EXTRA_RESULT_TEXT"
        const val BROADCAST_ACTION_SEND_TEXT = "WriteMessageTask.BROADCAST_ACTION_SEND_TEXT"

        const val EXTRA_RESULT_BUTTON = "EXTRA_RESULT_BUTTON"
        const val BROADCAST_ACTION_BTN_ENABLE = "CounterTask.BROADCAST_ACTION_SET_BUTTON_ENABLE"

        const val TEXTVIEW_STATE_KEY = "TEXTVIEW_STATE_KEY"
    }

    private var bindingMain: ActivityMainBinding? = null
    private var taskFragment: TasksFragment? = null

    private var messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val message = intent.getStringExtra(EXTRA_RESULT_TEXT)
            onNewMessageReceive(message)
        }
    }

    private var buttonReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val tasksInProgress = intent.getBooleanExtra(EXTRA_RESULT_BUTTON, false)
            checkButtonReceive(tasksInProgress)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain?.root)

        recoveryTextRotated(savedInstanceState)

        checkFragmentExist()

        clickListenerBtnStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEXTVIEW_STATE_KEY, bindingMain?.container?.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        messageReceiver.let { messageReceiver ->
            LocalBroadcastManager.getInstance(getInstanceApp()).registerReceiver(
                messageReceiver,
                IntentFilter(BROADCAST_ACTION_SEND_TEXT)
            )
        }

        buttonReceiver.let { buttonReceiver ->
            LocalBroadcastManager.getInstance(getInstanceApp()).registerReceiver(
                buttonReceiver,
                IntentFilter(BROADCAST_ACTION_BTN_ENABLE)
            )
        }
    }

    override fun onPause() {
        messageReceiver.let { messageReceiver ->
            LocalBroadcastManager.getInstance(getInstanceApp()).unregisterReceiver(messageReceiver)
        }
        buttonReceiver.let { buttonReceiver ->
            LocalBroadcastManager.getInstance(getInstanceApp()).unregisterReceiver(buttonReceiver)
        }
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingMain = null
    }

    private fun recoveryTextRotated(savedInstanceState: Bundle?) {
        var previousText = ""
        if (savedInstanceState != null && savedInstanceState.containsKey(TEXTVIEW_STATE_KEY)) {
            previousText = savedInstanceState.getString(TEXTVIEW_STATE_KEY).toString()
        }
        bindingMain?.container?.text = previousText
    }

    private fun onNewMessageReceive(message: String?) {
        if (!message.isNullOrEmpty()) {
            bindingMain?.container?.text = getText(message)
        }
    }

    private fun checkButtonReceive(tasksInProgress: Boolean) {
        if (tasksInProgress) {
            setButtonEnable()
        }
    }

    private fun getText(currentValue: String): String {
        val finalText = StringBuilder()

        finalText.apply {
            append(bindingMain?.container?.text)
            append(currentValue)
        }

        return finalText.toString()
    }

    private fun checkFragmentExist() {
        taskFragment =
            supportFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT) as TasksFragment?

        if (taskFragment == null) {
            taskFragment = TasksFragment()

            supportFragmentManager
                .beginTransaction()
                .add(taskFragment!!, TAG_TASK_FRAGMENT)
                .commit()
        }

        if (taskFragment?.getIsStarted() == true) {
            setBtnStartDisable()
        }
    }

    private fun clickListenerBtnStart() {
        bindingMain?.buttonStart?.setOnClickListener {
            setBtnStartDisable()
            taskFragment?.startAllAsyncTasks()
        }
    }

    private fun setButtonEnable() {
        bindingMain?.buttonStart?.apply {
            isEnabled = true
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
        }
    }

    private fun setBtnStartDisable() {
        bindingMain?.buttonStart?.apply {
            isEnabled = false
            setTextColor(ContextCompat.getColor(context, R.color.grey))
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
        }
    }
}