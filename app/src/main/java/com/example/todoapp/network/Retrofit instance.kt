package com.example.todoapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//  * Singleton object to initialize Retrofit
object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: TodoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApi::class.java)
    }
}
