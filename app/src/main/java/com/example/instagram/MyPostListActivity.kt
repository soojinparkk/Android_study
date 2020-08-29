package com.example.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.activity_my_post_list.*
import kotlinx.android.synthetic.main.activity_post_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPostListActivity : AppCompatActivity() {

    lateinit var myPostRecyclerview: RecyclerView
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post_list)

        myPostRecyclerview = myPost_recyclerview
        glide = Glide.with(this)
        createList()


        menu_allList2.setOnClickListener {
            startActivity(Intent(this, PostListActivity::class.java))
        }

        menu_upload2.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        menu_myInfo2.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }
    }

    fun createList() {
        (application as MasterApplication).service.getUserPosts()
            .enqueue(object : Callback<ArrayList<PostFromServer>> {
                override fun onResponse(
                    call: Call<ArrayList<PostFromServer>>,
                    response: Response<ArrayList<PostFromServer>>
                ) {
                    val myPostList = response.body()
                    val adapter = MyPostAdapter(myPostList!!,
                        LayoutInflater.from(this@MyPostListActivity), glide)
                    myPostRecyclerview.adapter = adapter
                    myPostRecyclerview.layoutManager = LinearLayoutManager(this@MyPostListActivity)
                }

                override fun onFailure(call: Call<ArrayList<PostFromServer>>, t: Throwable) {
                    Toast.makeText(this@MyPostListActivity, "MyPostList 실패", Toast.LENGTH_LONG).show()
                }
            })
    }
}

class MyPostAdapter(
    var postList:ArrayList<PostFromServer>,
    val inflater: LayoutInflater,
    val glide: RequestManager
): RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postOwner: TextView
        val postContent: TextView
        val postImage: ImageView

        init {
            postOwner = itemView.findViewById(R.id.post_owner)
            postContent = itemView.findViewById(R.id.post_content)
            postImage = itemView.findViewById(R.id.post_img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.post_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.postOwner.setText(postList.get(position).owner)
        holder.postContent.setText(postList.get(position).content)

        // glide에서 image를 불러옴
        glide.load(postList.get(position).image)
            .into(holder.postImage)
    }
}

