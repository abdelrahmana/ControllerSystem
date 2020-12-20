package com.smartangle.controllersystemapp.accountant.delegatecallcenter.model

data class AccDelegateOrderItems(
    val `data`: List<AccDelegateOrderItemsData>?
)

data class AccDelegateOrderItemsData(
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
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val total_quantity: String?
)

data class Category(
    val id: Int?,
    val name: String?
)