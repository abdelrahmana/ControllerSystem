package com.smartangle.controllersystemapp.common.delivery

data class AdminOrderItemsResponse(
    val `data`: List<DataAdminOrderItems>?
)

data class DataAdminOrderItems(
    val created_at: String?,
    val id: Int?,
    val order_id: String?,
    val price: String?,
    val product: ProductAdminOrderItems?,
    val product_id: String?,
    val quantity: String?,
    val updated_at: String?
)

data class ProductAdminOrderItems(
    val category: CategoryAdminOrderItems?,
    val category_id: String?,
    val currency: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val total_quantity: String?
)

data class CategoryAdminOrderItems(
    val id: Int?,
    val name: String?
)