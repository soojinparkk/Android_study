package com.example.instagram

import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    /*
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
    */

    @POST("user/signup/")
    @FormUrlEncoded
    fun register(
        @Field("username")username : String,
        @Field("password1")password1 : String,
        @Field("password2")password2 : String
    ): Call<UserFromServer>
}