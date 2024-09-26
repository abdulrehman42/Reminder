package com.ar.reminder.api

import com.ar.reminder.model.ListResponseModel
import com.ar.reminder.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(Constants.GET_LIST)
    suspend fun getListResponse(): Response<ListResponseModel>
}