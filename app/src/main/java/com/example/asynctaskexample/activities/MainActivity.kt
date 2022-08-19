package com.example.asynctaskexample.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.asynctaskexample.*
import com.example.asynctaskexample.databinding.ActivityMainBinding
import com.example.asynctaskexample.fragments.TasksFragment
import com.example.asynctaskexample.models.App.Companion.getInstanceApp
import com.example.asynctaskexample.models.App.Companion.setSubscribe

class MainActivity : AppCompatActivity() {
    private var bindingMain: ActivityMainBinding? = null
    private var taskFragment: TasksFragment? = null

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val message = intent.getStringExtra(EXTRA_RESULT_TEXT)
            onNewMessageReceive(message)
        }
    }

    private val buttonReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val tasksInProgress = intent.getBooleanExtra(EXTRA_RESULT_BUTTON, false)
            updateButtonState(tasksInProgress)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain?.root)

        restoreSavedText(savedInstanceState)

        checkFragmentExist()

        clickListenerBtnStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val currentTextValue = bindingMain?.container?.text

        if (!currentTextValue.isNullOrEmpty()) {
            outState.putString(TEXTVIEW_STATE_KEY, currentTextValue.toString())
        }

        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        setSubscribe(true)

        LocalBroadcastManager.getInstance(getInstanceApp()).registerReceiver(
            messageReceiver,
            IntentFilter(BROADCAST_ACTION_SEND_TEXT)
        )

        LocalBroadcastManager.getInstance(getInstanceApp()).registerReceiver(
            buttonReceiver,
            IntentFilter(BROADCAST_ACTION_BTN_ENABLE)
        )
    }

    override fun onPause() {
        super.onPause()
        setSubscribe(false)
        LocalBroadcastManager.getInstance(getInstanceApp()).unregisterReceiver(messageReceiver)
        LocalBroadcastManager.getInstance(getInstanceApp()).unregisterReceiver(buttonReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingMain = null
    }

    private fun restoreSavedText(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(TEXTVIEW_STATE_KEY)) {
            val previousText = savedInstanceState.getString(TEXTVIEW_STATE_KEY)
            bindingMain?.container?.text = previousText
        }
    }

    private fun onNewMessageReceive(message: String?) {
        if (!message.isNullOrEmpty()) {
            bindingMain?.container?.text = getText(message)
        }
    }

    private fun updateButtonState(tasksInProgress: Boolean) {
        if (tasksInProgress) {
            setButtonEnable()
        }
    }

    private fun getText(currentValue: String): String {
        return buildString {
            append(bindingMain?.container?.text)
            append(currentValue)
        }
    }

    private fun checkFragmentExist() {
        val fragment: TasksFragment =
            supportFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT) as TasksFragment?
                ?: run {
                    val taskFragment = TasksFragment()

                    supportFragmentManager
                        .beginTransaction()
                        .add(taskFragment, TAG_TASK_FRAGMENT)
                        .commit()

                    taskFragment
                }

        taskFragment = fragment

        if (fragment.getIsStarted()) {
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
            setTextColor(AppCompatResources.getColorStateList(context, R.color.white))
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
        }
    }

    private fun setBtnStartDisable() {
        bindingMain?.buttonStart?.apply {
            isEnabled = false
            setTextColor(AppCompatResources.getColorStateList(context, R.color.grey))
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
        }
    }
}