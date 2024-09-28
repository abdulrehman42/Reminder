package com.ar.reminder.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ar.reminder.R
import com.ar.reminder.databinding.ListItemBinding
import com.ar.reminder.model.ListResponseModel
import com.bumptech.glide.Glide

class ListingAdapter : ListAdapter<ListResponseModel.ResponseModelItem, ListingAdapter.ViewHolder>(RemindersCallback) {

    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ListResponseModel.ResponseModelItem) {
            Glide.with(binding.itemImage.context)
                .load(model.visualAidUrl)
                .placeholder(R.drawable.storeplaceholder)
                .into(binding.itemImage)

            binding.itemName.text = model.name
            binding.itemImage.setOnClickListener {
                itemClickCallback?.invoke(model._id)
            }
        }
    }

    private var itemClickCallback: ((String) -> Unit)? = null

    fun setOnItemClickCallback(callback: (String) -> Unit) {
        this.itemClickCallback = callback
    }

    object RemindersCallback : DiffUtil.ItemCallback<ListResponseModel.ResponseModelItem>() {
        override fun areItemsTheSame(oldItem: ListResponseModel.ResponseModelItem, newItem: ListResponseModel.ResponseModelItem): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: ListResponseModel.ResponseModelItem, newItem: ListResponseModel.ResponseModelItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}