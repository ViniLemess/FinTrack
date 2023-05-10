package org.fundatec.vinilemess.tcc.fintrackapp.data.remote

import org.fundatec.vinilemess.tcc.fintrackapp.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitNetworkClient {

    fun createNetworkClient(baseUrl: String = BuildConfig.FINTRACK_API): Retrofit =
        retrofitClient(
            baseUrl = baseUrl,
            httpClient = httpClient(),
            moshiConverter = moshi()
        )

    private fun moshi() =
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        )

    private fun httpClient():OkHttpClient {
        val duration = 60L
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptior())
            .connectTimeout(duration, TimeUnit.SECONDS)
            .readTimeout(duration, TimeUnit.SECONDS)
            .writeTimeout(duration, TimeUnit.SECONDS)
            .build()
    }


    private fun loggingInterceptior() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
         else HttpLoggingInterceptor.Level.NONE
    }

    private fun retrofitClient(
        baseUrl: String,
        httpClient: OkHttpClient,
        moshiConverter: MoshiConverterFactory
    ) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(moshiConverter)
        .build()

}