package com.example.instagram

import java.io.Serializable

// id, created도 있지만
// 필요한 부분만 받으면 됨
class PostFromServer(
    val owner: String? = null,
    val content: String? = null,
    val image: String? = null
): Serializable