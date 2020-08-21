package com.example.instagram

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication: Application() {

    lateinit var service: RetrofitService

    // activity의 onCreate()보다 먼저 실행
    // -> activity보다 application이 먼저 실행되기 때문
    // MasterApplication의 onCreate()에 retrofit을 설정하면,
    // 다른 activity에서 retrofit을 가져다 사용할 수 있음
    override fun onCreate() {
        super.onCreate()

        // chrome://inspect/#devices
        // 네트워크 통신을 보다 보기 편하게 도와주는 라이브러리
        // = Stetho
        Stetho.initializeWithDefaults(this)

        createRetrofit()
    }

    fun createRetrofit() {
        // header 설정 (header에 token이 있는 retrofit)
        // 원래 나가려던 통신을 original에 잡아둠
        // original에 header 추가 -> proceed
        val header = Interceptor {
            val original = it.request()

            if (checkIsLogin()) {
                getUserToken().let { token ->
                    val request = original.newBuilder()
                        .header("Authorization", "token $token")
                        .build()
                    it.proceed(request)
                }

            } else {
                it.proceed(original)
            }
        }

        // Stetho 사용
        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        // retrofit 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)     // Stetho 사용
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }


    // SharedPreferences에 token 값 저장되어 있음
    // 해당 key의 값(= token 값)을 불러옴
    // -> token 값이 없으면 login X
    private fun checkIsLogin(): Boolean {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("login_sp", "null")

        return token != "null"
    }

    // token 값 내보내는 함수
    private fun getUserToken(): String? {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("login_sp", "null")

        return if (token == "null") null
        else token
    }

}