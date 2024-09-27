package com.ar.reminder.views.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ar.reminder.databinding.ActivityMainBinding
import com.ar.reminder.notificationworker.scheduleNotifications
import com.ar.reminder.repository.NetworkResult
import com.ar.reminder.utils.Helper
import com.ar.reminder.viewmodel.ListViewModel
import com.ar.reminder.views.adapter.ListingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listingAdapter = ListingAdapter()
    private val listviewModel by viewModels<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        onclick()
        observer()
    }

    private fun observer() {
        listviewModel.listResponse.observe(this) { it ->
            when (it) {
                is NetworkResult.Success<*> -> {
                    it.data?.let {
                        try {
                            listingAdapter.submitList(it)
                            binding.listingRecylerview.apply {
                                layoutManager = LinearLayoutManager(
                                    this@ListingActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter = listingAdapter
                            }
                            scheduleNotifications(this@ListingActivity,it)

                        } catch (e: NullPointerException) {
                            print(e)
                        }
                    }

                    listviewModel.listResponse.value = null
                }

                is NetworkResult.Loading<*> -> {

                }

                is NetworkResult.Error<*> -> {
                    listviewModel.listResponse.value = null
                }

                else -> {
                }
            }
        }
    }

    private fun initView() {
        binding.time.text = Helper.getCurrentTime()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }

        listviewModel.listRequest()
    }


    private fun onclick() {
        listingAdapter.setOnItemClickCallback {
            listviewModel.loadItemData(it)
            startActivity(Intent(this@ListingActivity, DetailScreen::class.java))

        }
    }
}