package com.ar.reminder.views.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ar.reminder.R
import com.ar.reminder.databinding.ActivityMainBinding
import com.ar.reminder.notificationworker.scheduleNotifications
import com.ar.reminder.repository.NetworkResult
import com.ar.reminder.utils.Helper.getCurrentTime
import com.ar.reminder.viewmodel.ListViewModel
import com.ar.reminder.views.adapter.ListingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listingAdapter = ListingAdapter()
    private val listViewModel by viewModels<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        observeViewModel()
        handleClickEvents()
        startShimmer()
        listViewModel.fetchListData()
    }

    private fun startShimmer() {
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.listingRecylerview.visibility = View.GONE
    }

    private fun stopShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.listingRecylerview.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.listingRecylerview.apply {
            layoutManager =
                LinearLayoutManager(this@ListingActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = listingAdapter

            PagerSnapHelper().attachToRecyclerView(this)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val centerX = recyclerView.width / 2
                    for (i in 0 until recyclerView.childCount) {
                        val child = recyclerView.getChildAt(i)
                        val childCenterX = (child.left + child.right) / 2
                        val distanceFromCenter = Math.abs(centerX - childCenterX).toFloat()
                        val scale = 1f - (distanceFromCenter / centerX)
                        child.scaleX = 0.85f + scale * 0.15f
                        child.scaleY = 0.85f + scale * 0.15f
                    }
                }
            })
        }
        binding.time.text = getCurrentTime()

    }

    private fun observeViewModel() {
        listViewModel.listResponse.observe(this) { result ->
            stopShimmer()
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let {
                        try {
                            listingAdapter.submitList(it)
                            scheduleNotifications(this, it)
                        } catch (e: Exception) {
                            println(e)
                        }

                    }
                }

                is NetworkResult.Error -> {
                    stopShimmer()
                }

                is NetworkResult.Loading -> {
                    startShimmer()
                }
            }
        }
    }

    private fun handleClickEvents() {
        listingAdapter.setOnItemClickCallback { itemId ->
            listViewModel.loadItemDataLocal(itemId)
            startActivity(Intent(this, DetailScreen::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        binding.settingImageView.setOnClickListener {
            val intent = Intent(Settings.ACTION_DATE_SETTINGS)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        }
    }
}