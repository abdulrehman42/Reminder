package com.ar.reminder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ar.reminder.model.ListResponseModel
import com.ar.reminder.repository.ListingRepository
import com.ar.reminder.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val listingRepository: ListingRepository) :
    ViewModel() {
    val listResponse: MutableLiveData<NetworkResult<ListResponseModel>>
        get() = listingRepository.listResponse

    val getItemLocalResponse: MutableLiveData<NetworkResult<ListResponseModel.ResponseModelItem?>>
        get() = listingRepository.getItemLocalResponse

    fun fetchListData() {
        viewModelScope.launch {
            try {
                listingRepository.fetchListData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun loadItemDataLocal(id: String) {
        viewModelScope.launch {
            listingRepository.fetchLocalDataById(id)
        }
    }
}