package com.example.controllersystemapp.common.forgetpassword.model

data class RequestModelNewPass(
    var password: String?="",
    var password_confirmation: String?="",
    var phone: String?=""
)