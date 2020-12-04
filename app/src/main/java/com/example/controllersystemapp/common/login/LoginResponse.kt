package com.example.controllersystemapp.common.login

data class LoginResponse(
    val `data`: Data?
)

data class Data(
    val access_token: String?,
    val user: User?
)

data class User(
    val city: City?= City(),
    val city_id: String?="",
    val email: String?="",
    val enable_notification: String?="",
    val id: Int?=-1,
    val image: String?="",
    var lat: String?="0.0",
    var long: String?="0.0",
    val name: String?="",
    val phone: String?="",
    val role: Role?=Role(),
    val role_id: String?="",
    val status: String?=""
)

data class City(
    val id: Int?=-1,
    val name: String?=""
)

data class Role(
    val id: Int?=-1,
    val name: String?=""
)