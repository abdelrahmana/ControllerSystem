package com.smartangle.controllersystemapp.delegates.wallet.models

  data class DelegateOrderItemsListResponse(
    val `data`: List<DataDelegateOrderItems>?
)

data class DataDelegateOrderItems(
    val created_at: String?,
    val id: Int?,
    val order_id: String?,
    val price: String?,
    val product: ProductDelegateOrderItems?,
    val product_id: String?,
    val quantity: String?,
    val updated_at: String?
)

data class ProductDelegateOrderItems(
    val category: CategoryDelegateOrderItems?,
    val category_id: String?,
    val currency: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val total_quantity: String?
)

data class CategoryDelegateOrderItems(
    val id: Int?,
    val name: String?
)