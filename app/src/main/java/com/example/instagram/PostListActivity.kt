package com.example.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.activity_post_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.zip.Inflater

class PostListActivity : AppCompatActivity() {

    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)

        // glide 통해 image 띄우기
        glide = Glide.with(this)

        (application as MasterApplication).service.getAllPosts()
            .enqueue(object: Callback<ArrayList<PostFromServer>> {
                override fun onFailure(call: Call<ArrayList<PostFromServer>>, t: Throwable) {
                    Log.d("abcc", "adcc")

                }

                override fun onResponse(
                    call: Call<ArrayList<PostFromServer>>,
                    response: Response<ArrayList<PostFromServer>>
                ) {
                    if (response.isSuccessful) {
                        val postList = response.body()
                        val adapter = PostAdapter(postList!!, LayoutInflater.from(this@PostListActivity), glide)
                        post_recyclerview.adapter = adapter
                        post_recyclerview.layoutManager = LinearLayoutManager(this@PostListActivity)
                    }
                }
            })
    }

}

class PostAdapter(
    var postList:ArrayList<PostFromServer>,
    val inflater: LayoutInflater,
    val glide: RequestManager
): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

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