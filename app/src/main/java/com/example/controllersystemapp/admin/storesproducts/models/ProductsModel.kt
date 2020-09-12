package com.example.controllersystemapp.admin.storesproducts.models

//not used
data class ProductsModel(
    var name: String?,
    var price : String?,
    var currancy : String?,
    var quantity : Int?,
    var desc : String?,
    var isSelected : Int?=0
) {
}