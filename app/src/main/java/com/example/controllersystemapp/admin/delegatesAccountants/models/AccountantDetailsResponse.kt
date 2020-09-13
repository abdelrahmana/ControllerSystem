package com.example.controllersystemapp.admin.delegatesAccountants.models

data class AccountantDetailsResponse(
    val `data`: DetailsData?
)

data class DetailsData(
    val city: City?,
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

data class City(
    val id: Int?,
    val name: String?
)