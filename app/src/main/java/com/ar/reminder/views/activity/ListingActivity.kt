package com.ar.reminder.views.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ar.reminder.databinding.ActivityMainBinding
import com.ar.reminder.repository.NetworkResult
import com.ar.reminder.utils.Helper
import com.ar.reminder.viewmodel.ListViewModel
import com.ar.reminder.views.adapter.ListingAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val listingAdapter = ListingAdapter()
    val listviewModel by viewModels<ListViewModel>()
    private var mediaPlayer: MediaPlayer? = null
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
        listviewModel.listResponse.observe(this) {
            when (it) {
                is NetworkResult.Success<*> -> {
                    it.data?.let { data ->
                        try {
                            listingAdapter.submitList(it.data)
                            binding.listingRecylerview.apply {
                                layoutManager = LinearLayoutManager(
                                    this@ListingActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter = listingAdapter
                            }

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
        listviewModel.listRequest()

    }

    private fun onclick() {
        listingAdapter.setOnItemClickCallback {
            listviewModel.loadLocalData(it)
            startActivity(Intent(this@ListingActivity, DetailScreen::class.java))

        }
    }
}