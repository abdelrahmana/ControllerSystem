package com.smartangle.controllersystemapp.admin.settings.admin

data class AdminDetailsResponse(
    val `data`: DataAdminDetails?
)

data class DataAdminDetails(
    val city: City?,
    val city_id: String?,
    val delegate_orders: List<Any>?,
    val email: String?,
    val enable_notification: String?,
    val id: Int?,
    val image: String?,
    val lat: Any?,
    val long: Any?,
    val name: String?,
    val phone: String?,
    val role_id: String?,
    val status: String?,
    val warehouse_id: Any?
)

data class City(
    val id: Int?,
    val name: String?
)