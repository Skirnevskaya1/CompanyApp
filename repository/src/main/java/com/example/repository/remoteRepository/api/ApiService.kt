package com.example.repository.remoteRepository.api

import com.example.models.ConfigToRequest
import kotlinx.coroutines.Deferred
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("read-manager-companyapp")
    fun getNotificationAsync(@Query("config") config: ConfigToRequest): Deferred<String>
}
