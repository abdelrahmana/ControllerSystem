package com.smartangle.controllersystemapp.admin.settings.masrufat

data class ExpensesDetailsResponse(
    val `data`: DataDetails?
)

data class DataDetails(
    val created_at: String?,
    val details: String?,
    val id: Int?,
    val price: String?,
    val status: String?,
    val title: String?,
    val updated_at: String?,
    val user: User?,
    val user_id: String?
)

data class User(
    val city_id: String,
    val created_by: Any?,
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