package com.example.controllersystemapp.accountant.delegatecallcenter.debts

data class AccountantDebtsListResponse(
    val `data`: Data?
)

data class Data(
    val has_more_page: Boolean?,
    val list: List<AccountantDebtsList>?
)

data class AccountantDebtsList(
    val created_at: String?,
    val id: Int?,
    val product: Product?,
    val product_id: String?,
    val quantity: String?,
    val status: String?,
    val status_word: String?,
    val updated_at: String?,
    val user: User?,
    val user_id: String?
)

data class Product(
    val barcode: String?,
    val category_id: String?,
    val currency: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val price: String?,
    val total_quantity: String?
)

data class User(
    val city_id: String?,
    val created_by: String?,
    val email: Any?,
    val enable_notification: String?,
    val id: Int?,
    val image: String?,
    val lat: Any?,
    val long: Any?,
    val name: String?,
    val phone: String?,
    val role_id: String?,
    val status: String?,
    val warehouse_id: Int?
)