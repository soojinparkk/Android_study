package com.example.instagram

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.activity_user_info.*

class UploadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        upload_pic.setOnClickListener {
            getPicture()

        }



        menu_allList3.setOnClickListener {
            startActivity(Intent(this, PostListActivity::class.java))
        }

        menu_myList3.setOnClickListener {
            startActivity(Intent(this, MyPostListActivity::class.java))
        }

        menu_myInfo3.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }

    }

    fun getPicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000) {
            // uri = 자료의 위치 (url보다 상위 개념)
            // url = 웹 주소의 우치
            val uri: Uri = data!!.data!!
        }

        // uri -> 실제 파일이 위치한 주소가 아님 (상대 경로)
        // 실제 선택한 이미지(파일)의 위치를 찾아줘야 함 (절대 경로)
        fun getImageFilePath(contentUri: Uri): String {
            var columIndex = 0
            // projection -> 걸러내기 위해 사용
            val projection = arrayOf(MediaStore.Images.Media.DATA)

            // cursor -> List index에서 가리키기 위한 것
            // query -> 검색
            val cursor = contentResolver.query(contentUri, projection, null, null, null)
            if (cursor!!.moveToFirst()) {
                columIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            }

            // 파일의 절대 경로 return
            return cursor.getString(columIndex)
        }
    }
}