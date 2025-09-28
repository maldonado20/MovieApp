package com.example.movieapp.data.remote

import com.example.movieapp.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    //URL principal de API
    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"


    private val apiKeyInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url

        val urlConKey = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()

        val request = original.newBuilder()
            .url(urlConKey)
            .build()

        chain.proceed(request)
    }

    // Logs solo en debug
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(logging)
        .build()

    // Retrofit listo
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(TMDB_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tmdbService: TmdbService = retrofit.create(TmdbService::class.java)
}