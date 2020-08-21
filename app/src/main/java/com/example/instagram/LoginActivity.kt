package com.example.instagram

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 회원가입 화면으로 이동
        hereBtn.setOnClickListener {
            val intent = Intent(this, EmailSignupActivity::class.java)
            startActivity(intent)
        }

        login_Btn.setOnClickListener {

            val username = login_id_inputBox.text.toString()
            val password = login_pw_inputBox.text.toString()

            (application as MasterApplication).service.login(username, password)
                .enqueue(object : Callback<UserFromServer> {
                    override fun onFailure(call: Call<UserFromServer>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<UserFromServer>,
                        response: Response<UserFromServer>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "로그인 완료", Toast.LENGTH_LONG).show()

                            val user = response.body()
                            val token = user!!.token!!
                            saveUserToken(token, this@LoginActivity)

                            (application as MasterApplication).createRetrofit()
                        }
                    }
                })
        }
    }

    fun saveUserToken(token: String, activity: Activity) {
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }
}
