package com.ar.reminder.application

import android.app.Application
import com.ar.reminder.viewmodel.ListViewModel
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ReminderApp: Application() {
    companion object {
        lateinit var instance: ReminderApp
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}