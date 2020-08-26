package com.example.instagram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_post_list.*
import kotlinx.android.synthetic.main.activity_post_list.menu_myInfo
import kotlinx.android.synthetic.main.activity_post_list.menu_myList
import kotlinx.android.synthetic.main.activity_post_list.menu_upload
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        logout_Btn.setOnClickListener {
            val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("login_sp", "null")
            editor.commit()

            (application as MasterApplication).createRetrofit()
            // 이 activity를 종료함 = finish()
            finish()

            startActivity(Intent(this, LoginActivity::class.java))
        }

        menu_allList4.setOnClickListener {
            startActivity(Intent(this, PostListActivity::class.java))
        }

        menu_myList4.setOnClickListener {
            startActivity(Intent(this, MyPostListActivity::class.java))
        }

        menu_upload4.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

    }
}