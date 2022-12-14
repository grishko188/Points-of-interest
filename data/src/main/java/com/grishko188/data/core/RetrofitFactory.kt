package com.grishko188.data.core

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.Executor

object RetrofitFactory {

    fun create(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        url: String,
        executor: Executor? = null
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .also { builder ->
                if (executor != null) {
                    builder.callbackExecutor(executor)
                }
            }
            .build()
    }
}