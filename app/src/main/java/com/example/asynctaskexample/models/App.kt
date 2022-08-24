package com.example.asynctaskexample.models

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        self = this
    }

    companion object {
        private lateinit var self: App
        private var subscribe: Boolean = false

        fun getInstanceApp(): App {
            return self
        }

        fun setSubscribe(isSubscribe: Boolean) {
            subscribe = isSubscribe
        }

        fun isSubscribe(): Boolean {
            return subscribe
        }
    }
}