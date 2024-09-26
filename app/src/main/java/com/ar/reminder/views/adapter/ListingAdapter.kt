package com.ar.reminder.views.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ar.reminder.R
import com.ar.reminder.databinding.ListItemBinding
import com.ar.reminder.model.ListResponseModel
import com.ar.reminder.utils.Helper
import com.bumptech.glide.Glide

class ListingAdapter() :
    ListAdapter<ListResponseModel.ResponseModelItem, ListingAdapter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ListResponseModel.ResponseModelItem) {
            Glide.with(binding.itemImage.context)
                .load(model.visualAidUrl).placeholder(R.drawable.storeplaceholder)
                .into(binding.itemImage)
            binding.itemImage.setOnClickListener {
                itemClickCallback?.invoke(model._id)
            }
            binding.playBtn.setOnClickListener {
                model.notifsV2SoundUrl?.let {
                    Helper.playMp3FromUrl(binding.root.context, it, true)
                }
            }
        }
    }

    var itemClickCallback: ((String) -> Unit)? = null
    fun setOnItemClickCallback(callback: (String) -> Unit) {
        this.itemClickCallback = callback
    }


    object TemplateDiffCallback : DiffUtil.ItemCallback<ListResponseModel.ResponseModelItem>() {
        override fun areItemsTheSame(
            oldItem: ListResponseModel.ResponseModelItem,
            newItem: ListResponseModel.ResponseModelItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ListResponseModel.ResponseModelItem,
            newItem: ListResponseModel.ResponseModelItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

}