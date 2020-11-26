package com.example.controllersystemapp.delegates.makeorder.model

data class DelegateProductsListResponse(
    val `data`: List<Data>?
)

data class Data(
    val barcode: String?,
    val category: Category?,
    val category_id: Int?,
    val currency: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val price: String?,
    var total_quantity: String?,
    val ware_houses: List<WareHouse>?,
    var selectedQuantity: Int?=1

)

data class Category(
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