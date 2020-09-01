package com.example.controllersystemapp.common.login

data class LoginRequest(

    val phone : String? ="",
    var password : String? ="",
    var device_name : String? ="",
    var device_token : String? =""
)