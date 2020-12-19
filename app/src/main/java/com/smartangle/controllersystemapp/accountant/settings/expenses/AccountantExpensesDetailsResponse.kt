package com.smartangle.controllersystemapp.accountant.settings.expenses

data class AccountantExpensesDetailsResponse(
    val `data`: DataExpensesDetails?
)

data class DataExpensesDetails(
    val created_at: String?="",
    val details: String?="",
    val id: Int?=-1,
    val price: String?="",
    val status: String?="",
    val title: String?="",
    val updated_at: String?="",
    val user: User?,
    val user_id: String?=""
)

data class User(
    val city_id: String?="",
    val email: String?="",
    val enable_notification: String?="",
    val id: Int?=-1,
    val image: String?="",
    val lat: Any?,
    val long: Any?,
    val name: String?="",
    val phone: String?="",
    val role_id: String?="",
    val status: String?=""
)