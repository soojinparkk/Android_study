package com.example.instagram

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadActivity : AppCompatActivity() {

    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        // 사진 불러오기
        upload_pic.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없는 경우
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
                Toast.makeText(this@UploadActivity, "Upload 권한 없음", Toast.LENGTH_LONG).show()
            } else {
                // 권한이 있는 경우
                Toast.makeText(this@UploadActivity, "Upload 권한 있음", Toast.LENGTH_LONG).show()
                getPicture()
            }
        }

        // 사진 업로드하기
        upload.setOnClickListener {
            uploadPost()
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

    // READ_EXTERNAL_STORAGE
    // 위의 권한 허용해줘야 함
    // 애뮬레이터 내에서 강제 허용도 가능
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
            filePath = getImageFilePath(uri)
        }
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

    fun uploadPost() {
        // File(찾을 파일 주소) -> File 클래스가 알아서 파일을 찾아줌
        val file = File(filePath)
        // image라는 타입 정해주고, 실제 찾은 file을 넣어줌
        val fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        // "image" -> 서버한테 보낼 때 사용할 이름
        // MultipartBody -> Body가 여러개
        // 이미지를 보낼 때 하나만 딱 보내는 게 아니고 여러개 쪼개서 보냄
        val part = MultipartBody.Part.createFormData("image", file.name, fileRequestBody)
        val content = RequestBody.create(MediaType.parse("text/plain"), getContent())

        (application as MasterApplication).service.uploadPost(
            part, content
        ).enqueue(object : Callback<PostFromServer> {
            override fun onResponse(
                call: Call<PostFromServer>,
                response: Response<PostFromServer>
            ) {
                if (response.isSuccessful) {
                    // val post = response.body()
                    // Log.d("pathh", post!!.content)
                    finish()
                    startActivity(Intent(this@UploadActivity, MyPostListActivity::class.java))
                }
            }

            override fun onFailure(call: Call<PostFromServer>, t: Throwable) {
                Toast.makeText(this@UploadActivity, "Upload 실패", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getContent(): String {
        return upload_content_input.text.toString()
    }

    // 권한 요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 승인
            } else {
                // 거부
            }
        }
    }
}