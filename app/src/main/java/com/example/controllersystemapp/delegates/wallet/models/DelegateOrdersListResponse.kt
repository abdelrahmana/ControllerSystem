package com.example.controllersystemapp.delegates.wallet.models

data class DelegateOrdersListResponse(
    val `data`: List<Data>?
)

data class Data(
    val address: String?,
    val created_at: String?,
    val created_by: Int?,
    val currency: String?,
    val delegate_id: Int?,
    val details: List<Detail>?,
    val email: String?,
    val id: Int?,
    val name: String?,
    val order_number: Int?,
    val phone: String?,
    val shipment_cost: String?,
    val status: Int?,
    val status_word: String?,
    val total_price: String?,
    val updated_at: String?
)

data class Detail(
    val created_at: String?,
    val id: Int?,
    val order_id: Int?,
    val price: String?,
    val product: Product?,
    val product_id: Int?,
    val quantity: Int?,
    val updated_at: String?
)

data class Product(
    val category: Category?,
    val category_id: Int?,
    val currency: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val price: String?,
    val total_quantity: String?
)

data class Category(
    val id: Int?,
    val name: String?,
    val parent_id: Any?
)