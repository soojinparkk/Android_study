package com.example.instagram

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class EmailSignupActivity : AppCompatActivity() {

    lateinit var usernameView: EditText
    lateinit var userPasswordView: EditText
    lateinit var userPassword2View: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_signup)

        initView(this)

    }

    fun initView(activity: Activity) {
        usernameView = activity.findViewById(R.id.username_inputBox)
        userPasswordView = activity.findViewById(R.id.password1_inputBox)
        userPassword2View = activity.findViewById(R.id.password2_inputBox)
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
