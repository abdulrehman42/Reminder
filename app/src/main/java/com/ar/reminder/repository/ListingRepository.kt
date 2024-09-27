package com.ar.reminder.repository

import androidx.lifecycle.MutableLiveData
import com.ar.reminder.api.ApiService
import com.ar.reminder.roomdatabase.ListDao
import com.ar.reminder.model.ListResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListingRepository @Inject constructor(
    private val apiService: ApiService,
    private val responseModelDao: ListDao
) {
    private val _listResponse = MutableLiveData<NetworkResult<ListResponseModel>>()
    val listResponse: MutableLiveData<NetworkResult<ListResponseModel>>
        get() = _listResponse

    private val _listLocalResponse =
        MutableLiveData<NetworkResult<List<ListResponseModel.ResponseModelItem?>>>()
    val listLocalResponse: MutableLiveData<NetworkResult<List<ListResponseModel.ResponseModelItem?>>>
        get() = _listLocalResponse

    private val _getItemLocalResponse =
        MutableLiveData<NetworkResult<ListResponseModel.ResponseModelItem?>>()
    val getItemLocalResponse: MutableLiveData<NetworkResult<ListResponseModel.ResponseModelItem?>>
        get() = _getItemLocalResponse

    suspend fun getList() {
        _listResponse.postValue(NetworkResult.Loading())
        val response = apiService.getListResponse()
        if (response.isSuccessful) {
            insertIntoDatabase(response.body())
            _listResponse.postValue(NetworkResult.Success(response.body()!!))

        } else {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _listResponse.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
    }

    suspend fun insertIntoDatabase(body: ListResponseModel?) {
        body?.let {
            withContext(Dispatchers.IO)
            {
                val listResponseModel = it
                val responseEntityList = listResponseModel.map {
                    ListResponseModel.ResponseModelItem(
                        _id = it._id,
                        __v = it.__v,
                        clientId = it.clientId,
                        createdAt = it.createdAt,
                        createdBy = it.createdBy,
                        ctaOrdering = it.ctaOrdering,
                        devices = it.devices,
                        duration = it.duration,
                        enableVacationMode = it.enableVacationMode,
                        entityType = it.entityType,
                        folder = it.folder,
                        folderId = it.folderId,
                        isScheduledByV2 = it.isScheduledByV2,
                        label = it.label,
                        lastSchedule = it.lastSchedule,
                        libraryType = it.libraryType,
                        limitRunPerDay = it.limitRunPerDay,
                        limitRunPerHour = it.limitRunPerHour,
                        name = it.name,
                        narration = it.narration,
                        notifications = it.notifications,
                        notifsV2SoundName = it.notifsV2SoundName,
                        notifsV2SoundUrl = it.notifsV2SoundUrl,
                        numberOfRunPerDay = it.numberOfRunPerDay,
                        numberOfRunPerHour = it.numberOfRunPerHour,
                        ordering = it.ordering,
                        reminderNotificationsV2 = it.reminderNotificationsV2,
                        schedule = it.schedule,
                        scheduleV2 = it.scheduleV2,
                        tags = it.tags,
                        type = it.type,
                        updatedAt = it.updatedAt,
                        visualAidUrl = it.visualAidUrl
                    )
                }
                responseModelDao.insert(responseEntityList)
            }

        }

    }

    suspend fun getLocalDataById(id: String) {
        withContext(Dispatchers.IO){
            _getItemLocalResponse.postValue(NetworkResult.Success(responseModelDao.getResponseById(id)))
        }

    }
    suspend fun getLocalData(id: String) {
        withContext(Dispatchers.IO){
            _listLocalResponse.postValue(NetworkResult.Success(responseModelDao.getListResponse()))
        }

    }
}