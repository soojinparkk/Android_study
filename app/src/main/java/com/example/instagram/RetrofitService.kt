package com.example.instagram

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {
    // 어노테이션
    @GET("json/students/")
    fun getStudentsList(): Call<ArrayList<PersonFromServer>>

    @POST("json/students/")
    fun createStudent(
        @Body params : HashMap<String, Any>
    ): Call<PersonFromServer>

    @POST("json/students/")
    fun createStudentEasy(
        @Body person : PersonFromServer
    ): Call<PersonFromServer>

    @POST("user/signup/")
    fun register(
        @Body register : RegisterFromServer
    ): Call<UserFromServer>
}