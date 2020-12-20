package com.smartangle.controllersystemapp.accountant.delegatecallcenter.model

data class AccDelegateOrderItemsDetails(
    val `data`: DataAccDelegateOrderItemsDetails?
)

data class DataAccDelegateOrderItemsDetails(
    val created_at: String?,
    val id: Int?,
    val order: Order?,
    val order_id: String?,
    val price: String?,
    val product: ProductDetails?,
    val product_id: String?,
    val quantity: String?,
    val updated_at: String?
)

data class Order(
    val address: String?,
    val created_at: String?,
    val created_by: String?,
    val currency: String?,
    val delegate_id: String?,
    val email: String?,
    val id: Int?,
    val name: String?,
    val order_number: String?,
    val phone: String?,
    val shipment_cost: String?,
    val status: String?,
    val status_word: String?,
    val total_price: String?,
    val updated_at: String?
)

data class ProductDetails(
    val barcode: String,
    val category: CategoryDetails,
    val category_id: String,
    val currency: String,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val total_quantity: String
)

data class CategoryDetails(
    val id: Int,
    val name: String,
    val parent_id: String
)