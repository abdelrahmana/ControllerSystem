package com.example.controllersystemapp.admin.storesproducts.models

data class ProductsDetailsResponse(
    val `data`: DataProdDetails?
)

data class DataProdDetails(
    val barcode: String?="",
    val category: Category?,
    val category_id: String?="",
    val currency: String?="",
    val description: String?="",
    val id: Int?=0,
    val image: String?="",
    val images: List<Image>?,
    val name: String?="",
    val price: String?="",
    val total_quantity: String?="",
    val ware_houses: List<WareHouse>?
)

data class Category(
    val id: Int?=0,
    val name: String?=""
)

data class Image(
    val id: Int?=0,
    val image: String?="",
    val product_id: String?=""
)

//data class WareHouse(
//    val accountant_id: String,
//    val address: String,
//    val id: Int,
//    val name: String,
//    val pivot: Pivot
//)
//
//data class Pivot(
//    val id: String,
//    val product_id: String,
//    val quantity: String,
//    val warehouse_id: String
//)