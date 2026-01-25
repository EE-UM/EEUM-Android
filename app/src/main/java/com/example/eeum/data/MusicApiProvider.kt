package com.example.eeum.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MusicApiProvider {
    private const val BASE_URL = "https://www.eeum.xyz/dev/"

    private val gson: Gson = GsonBuilder().create()

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api: MusicApi = retrofit.create(MusicApi::class.java)
}
