package com.smartangle.controllersystemapp.common.delivery

data class AdminOrdersListResponse(
    val `data`: List<Data>?
)

data class Data(
    val accountant_id: Any?,
    val address: Any?,
    val created_at: String?,
    val created_by: String?,
    val currency: String?,
    val delegate_id: String?,
    val details: List<Detail>?,
    val email: String?,
    val id: Int?,
    val lat: Any?,
    val long: Any?,
    val name: String?,
    val order_number: String?,
    val phone: String?,
    val shipment_cost: String?,
    val status: String?,
    val status_word: String?,
    val total_price: String?,
    val updated_at: String?
)

data class Detail(
    val created_at: String?,
    val id: Int?,
    val order_id: String?,
    val price: String?,
    val product: Product?,
    val product_id: String?,
    val quantity: String?,
    val updated_at: String?
)

data class Product(
    val category: Category?,
    val category_id: String?,
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