package com.ar.reminder.views.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.ar.reminder.R
import com.ar.reminder.databinding.ActivityDetailScreenBinding
import com.ar.reminder.utils.Helper
import com.ar.reminder.utils.Helper.currentDaySchedule
import com.ar.reminder.viewmodel.ListViewModel
import com.ar.reminder.views.activity.base.BaseActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class DetailScreen : BaseActivity() {
    private lateinit var binding: ActivityDetailScreenBinding
    private val listViewModel by viewModels<ListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTimeTextView(binding.currentTime)
        observerData()
        handleClickEvents()
    }
    private fun observerData() {
        listViewModel.getItemLocalResponse.observe(this) { response ->
            response.data?.let { item ->
                binding.apply {
                    startTime.text = if (currentDaySchedule(item) == "No schedule available") {
                        item.scheduleV2?.timeValue ?: ""
                    } else {
                        currentDaySchedule(item)
                    }
                    itemName.text = item.name
                    val soundUrl = item.notifsV2SoundUrl
                    voiceId.setOnClickListener {
                        soundUrl?.let { sound ->
                            Helper.playMp3FromUrl(sound)
                        }
                    }
                    Glide.with(this@DetailScreen)
                        .load(item.visualAidUrl)
                        .placeholder(R.drawable.storeplaceholder)
                        .into(imageId)
                }
            }
        }
    }

    private fun handleClickEvents() {
        binding.okBtn.setOnClickListener {
            finish()
            startFadeInOutTransition()
        }
    }
}