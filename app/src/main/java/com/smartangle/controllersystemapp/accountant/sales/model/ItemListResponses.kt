package com.smartangle.controllersystemapp.accountant.sales.model

data class ItemListResponses(
    var `data`: List<Datax>?
)

data class Datax(
    var created_at: String?,
    var id: Int?,
    var order_id: Int?,
    var price: String?,
    var product: Productx?,
    var product_id: Int?,
    var quantity: Int?,
    var updated_at: String?
)

data class Productx(
    var category: Categoryx?,
    var category_id: Int?,
    var currency: String?,
    var description: String?,
    var id: Int?,
    var image: String?,
    var name: String?,
    var total_quantity: String?
)

data class Categoryx(
    var id: Int?,
    var name: String?
)