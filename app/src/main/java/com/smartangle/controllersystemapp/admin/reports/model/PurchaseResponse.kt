package com.smartangle.controllersystemapp.admin.reports.model

data class PurchaseResponse(
    var countPurchases: Int?,
    var `data`: ArrayList<Datax>?,
    var finalTotal: Int?,
    var month: Int?,
    var year: Int?
)

data class Datax(
    var barcode: String?,
    var category_id: String?,
    var currency: String?,
    var description: String?,
    var id: Int?,
    var image: String?,
    var main_price: String?,
    var name: String?,
    var price: String?,
    var purchases_value: Int?,
    var total_quantity: String?
)
