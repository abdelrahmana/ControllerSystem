package com.example.controllersystemapp.admin.delegatesAccountants.models

data class AccountantListResponse(
    val `data`: Data?
)

data class Data(
    val count: Int?,
    val list: List<AccountantData>?
)

data class AccountantData(
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