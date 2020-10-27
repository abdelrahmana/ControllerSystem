package com.example.controllersystemapp.accountant.settings.expenses

data class AccountantExpensesListResponse(
    val `data`: List<Data>?
)

data class Data(
    val created_at: String?="",
    val details: String?="",
    val id: Int?=-1,
    val price: String?="",
    val status: String?="",
    val title: String?="",
    val updated_at: String?="",
    val user_id: String?=""
)