package com.example.instagram

import java.io.Serializable

class UserFromServer (
    var username: String? = null,
    var token: String? = null
): Serializable