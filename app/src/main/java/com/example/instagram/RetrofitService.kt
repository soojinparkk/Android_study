package com.example.instagram

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    /*
    @POST("user/signup/")
    fun register(
        @Body register : RegisterToServer
    ): Call<UserFromServer>
    */

    // @Headers("content-type: application/x-www-form-urlencoded")

    @POST("user/signup/")
    @FormUrlEncoded
    fun register(
        @Field("username")username : String,
        @Field("password1")password1 : String,
        @Field("password2")password2 : String
    ): Call<UserFromServer>

    @POST("user/login/")
    @FormUrlEncoded
    fun login(
        @Field("username")username: String,
        @Field("password")password: String
    ): Call<UserFromServer>

    @GET("instagram/post/list/all/")
    fun getAllPosts(): Call<ArrayList<PostFromServer>>

    @Multipart
    @POST("instagram/post/")
    fun uploadPost(
        @Part image: MultipartBody.Part,
        @Part("content")requestBody: RequestBody
    ): Call<PostFromServer>

    @GET("instagram/post/list/")
    fun getUserPosts(): Call<ArrayList<PostFromServer>>

}