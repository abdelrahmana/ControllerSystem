package com.smartangle.controllersystemapp.admin.settings.masrufat

data class ExpensesListResponse(
    val `data`: List<Data>?
)

data class Data(
    val created_at: String?,
    val details: String?,
    val id: Int?,
    val price: String?,
    val status: String?,
    val title: String?,
    val updated_at: String?,
    val user_id: String?
)