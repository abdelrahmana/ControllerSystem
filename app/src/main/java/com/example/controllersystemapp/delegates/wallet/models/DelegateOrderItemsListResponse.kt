package com.example.controllersystemapp.delegates.wallet.models

data class DelegateOrderItemsListResponse(
    val `data`: List<ItemsData>?
)

data class ItemsData(
    val barcode: String?,
    val category: ItemsCategory?,
    val category_id: Int?,
    val currency: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val price: String?,
    val total_quantity: String?,
    val ware_houses: List<WareHouse>?
)

data class ItemsCategory(
    val id: Int?,
    val name: String?
)

data class WareHouse(
    val accountant_id: Int?,
    val address: String?,
    val id: Int?,
    val name: String?,
    val pivot: Pivot?
)

data class Pivot(
    val id: Int?,
    val product_id: Int?,
    val quantity: Int?,
    val warehouse_id: Int?
)