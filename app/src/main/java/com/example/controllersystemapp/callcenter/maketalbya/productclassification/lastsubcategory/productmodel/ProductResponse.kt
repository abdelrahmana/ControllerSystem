package com.example.controllersystemapp.callcenter.maketalbya.productclassification.lastsubcategory.productmodel

data class ProductResponse(
    var `data`: ArrayList<Datas>?
)

data class Datas(
    var barcode: String?,
    var category: Category?,
    var category_id: Int?,
    var currency: String?,
    var description: String?,
    var id: Int?,
    var image: String?,
    var name: String?,
    var price: String?,
    var total_quantity: String?,
    var ware_houses: List<WareHouse>?
)

data class Category(
    var id: Int?,
    var name: String?
)

data class WareHouse(
    var accountant_id: Int?,
    var address: String?,
    var id: Int?,
    var name: String?,
    var pivot: Pivot?
)

data class Pivot(
    var id: Int?,
    var product_id: Int?,
    var quantity: Int?,
    var warehouse_id: Int?
)