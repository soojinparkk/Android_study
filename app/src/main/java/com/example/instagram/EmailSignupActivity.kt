package com.example.instagram

import android.app.Activity
import android.content.Context
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
        setupListener()
    }

    fun setupListener() {
        registerBtn.setOnClickListener {
            register(this)
        }
    }

    fun register(activity: Activity) {
        val username = usernameView.text.toString()
        val password1 = userPasswordView.text.toString()
        val password2 = userPassword2View.text.toString()
        val registerVal = RegisterFromServer(username, password1, password2)


        (application as MasterApplication).service.register(registerVal).enqueue(object : Callback<UserFromServer> {
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
                }
            }
        })
    }

    fun saveUserToken(token: String, activity: Activity) {
        val sp = activity.getSharedPreferences("logon_sp", Context.MODE_PRIVATE)
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
