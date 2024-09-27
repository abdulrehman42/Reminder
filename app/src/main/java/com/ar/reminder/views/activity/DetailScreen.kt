package com.ar.reminder.views.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.ar.reminder.R
import com.ar.reminder.databinding.ActivityDetailScreenBinding
import com.ar.reminder.utils.Helper
import com.ar.reminder.viewmodel.ListViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailScreen : AppCompatActivity() {
    lateinit var binding: ActivityDetailScreenBinding
    val listViewModel by viewModels<ListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        observer()
    }

    private fun initView() {
        binding.apply {
            currentTime.text = Helper.getCurrentTime()
        }
    }

    private fun observer() {
        listViewModel.getItemLocalResponse.observe(this) {
            binding.apply {
                it.data?.let {
                    it.scheduleV2?.let {
                        if (it.timeValue!=null)
                        {
                            startTime.text = it.timeValue
                        }else{
                            textView2.isGone=true
                            linearLayout2.isGone=true
                        }
                    }
                    Glide.with(this@DetailScreen).load(it.visualAidUrl)
                        .placeholder(R.drawable.storeplaceholder).into(imageId)
                }

               // scheduleNotifications(this@DetailScreen,it.data)

            }
        }
    }
}