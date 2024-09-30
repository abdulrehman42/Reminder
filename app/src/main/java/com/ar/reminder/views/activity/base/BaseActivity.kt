package com.ar.reminder.views.activity.base

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ar.reminder.R
import com.ar.reminder.utils.Helper.getCurrentTime

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    protected fun setTimeTextView(textView: TextView) {
        textView.text = getCurrentTime()
    }
    protected fun startFadeInOutTransition() {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    protected fun handleSettingsClick(intentAction: String) {
        val intent = Intent(intentAction)
        startActivity(intent)
        startFadeInOutTransition()
    }
}