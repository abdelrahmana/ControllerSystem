package com.smartangle.controllersystemapp.common.delivery

data class AdminOrderItemDetailsResponse(
    val `data`: DataAdminOrderItemDetails?
)

data class DataAdminOrderItemDetails(
    val created_at: String?,
    val id: Int?,
    val order: Order?,
    val order_id: String?,
    val price: String?,
    val product: ProductAdminOrderItemDetails?,
    val product_id: String?,
    val quantity: String?,
    val updated_at: String?
)

data class Order(
    val accountant_id: Any?,
    val address: String?,
    val client: Any?,
    val client_id: Any?,
    val created_at: String?,
    val created_by: String?,
    val currency: String?,
    val `delegate`: Delegate?,
    val delegate_id: String?,
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

data class ProductAdminOrderItemDetails(
    val barcode: String?,
    val category: CategoryAdminOrderItemDetails?,
    val category_id: String?,
    val currency: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val total_quantity: String?
)

data class Delegate(
    val id: Int?,
    val name: String?,
    val phone: String?,
    val warehouse_id: Any?
)

data class CategoryAdminOrderItemDetails(
    val id: Int?,
    val name: String?,
    val parent_id: Any?
)