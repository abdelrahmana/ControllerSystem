package com.smartangle.controllersystemapp.admin.storesproducts.models

data class ProductsListResponse(
    val `data`: List<Data>?
)

data class Data(
    val barcode: String?,
    val category_id: String?,
    val currency: String?,
    val description: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val price: String?,
    val total_quantity: String?,
    val ware_houses: List<WareHouse>?,
    var totalSelectedProduct : String?="0"
)

data class WareHouse(
    val accountant_id: String?,
    val address: String?,
    val id: Int?,
    val name: String?,
    val pivot: Pivot?
)

data class Pivot(
    val id: String?,
    val product_id: String?,
    val quantity: String?,
    val warehouse_id: String?
)