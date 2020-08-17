package com.example.instagram

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailSignupActivity : AppCompatActivity() {

    lateinit var usernameView: EditText
    lateinit var userPasswordView: EditText
    lateinit var userPassword2View: EditText
    lateinit var registerBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_signup)

        initView(this)
        setupListener(this)
    }

    fun setupListener(activity: Activity) {
        registerBtn.setOnClickListener {
            register(this)
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun register(activity: Activity) {
        val username = getUserName()
        val password1 = getUserPassword1()
        val password2 = getUserPassword2()
        // val registerVal = RegisterFromServer(username, password1, password2)


        (application as MasterApplication).service.register(username, password1, password2)
            .enqueue(object : Callback<UserFromServer> {
            override fun onFailure(call: Call<UserFromServer>, t: Throwable) {
                Toast.makeText(activity, "가입 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<UserFromServer>,
                response: Response<UserFromServer>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "가입 성공", Toast.LENGTH_LONG).show()
                    val user = response.body()
                    val token = user!!.token!!
                    saveUserToken(token, activity)

                    (application as MasterApplication).createRetrofit()
                }
            }
        })
    }

    fun saveUserToken(token: String, activity: Activity) {
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }

    fun initView(activity: Activity) {
        usernameView = activity.findViewById(R.id.username_inputBox)
        userPasswordView = activity.findViewById(R.id.password1_inputBox)
        userPassword2View = activity.findViewById(R.id.password2_inputBox)
        registerBtn = activity.findViewById(R.id.signup_Btn)
    }

    fun getUserName(): String {
        return usernameView.text.toString()
    }

    fun getUserPassword1(): String {
        return userPasswordView.text.toString()
    }

    fun getUserPassword2(): String {
        return userPassword2View.text.toString()
    }

}
