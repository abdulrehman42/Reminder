package com.ar.reminder.views.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.ar.reminder.R
import com.ar.reminder.databinding.ActivityDetailScreenBinding
import com.ar.reminder.utils.Helper
import com.ar.reminder.utils.Helper.currentDaySchedule
import com.ar.reminder.utils.Helper.getCurrentTime
import com.ar.reminder.viewmodel.ListViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailScreen : AppCompatActivity() {

    private lateinit var binding: ActivityDetailScreenBinding
    private val listViewModel by viewModels<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        observeViewModel()
    }

    private fun initView() {
        binding.currentTime.text = getCurrentTime()
        binding.okBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun observeViewModel() {
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

}