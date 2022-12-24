package com.grishko188.data.features.poi.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface WizardServiceApi {

    @GET
    suspend fun getUrlContent(@Url contentUrl: String): ResponseBody
}