package com.smartangle.controllersystemapp.common.login

import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData

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
    val creator : CallCenterDelegateData?=null, // to use it in all places
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
data class Creator(
    var city_id: String?,
    var created_by: String?,
    var email: String?,
    var enable_notification: String?,
    var id: Int?,
    var image: String?,
    var lat: String?,
    var long: String?,
    var name: String?,
    var phone: String?,
    var role_id: String?,
    var status: String?,
    var warehouse_id: String?
)