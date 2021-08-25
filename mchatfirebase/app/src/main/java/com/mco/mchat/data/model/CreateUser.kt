package com.mco.mchat.data.model

data class CreateUser(
    var displayName: String = "",
    var email: String = "",
    var password: String = ""
)