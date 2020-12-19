package com.smartangle.controllersystemapp.admin.settings.admin

data class AdminListResponse(
    val `data`: Data?
)

data class Data(
    val count: Int?,
    val list: List<Admin>?
)

data class Admin(
    val city_id: String?,
    val email: String?,
    val enable_notification: String?,
    val id: Int?,
    val image: String?,
    val lat: String?,
    val long: String?,
    val name: String?,
    val phone: String?,
    val role_id: String?,
    val status: String?
)