package com.example.controllersystemapp.accountant.sales.model

data class SalesResponse(
    var `data`: ArrayList<Data>?
)

data class Data(
    var address: String?,
    var created_at: String?,
    var created_by: Int?,
    var currency: String?,
    var delegate_id: Int?,
    var details: List<Detail>?,
    var email: String?,
    var id: Int?,
    var name: String?,
    var order_number: Int?,
    var phone: String?,
    var shipment_cost: String?,
    var status: Int?,
    var status_word: String?,
    var total_price: String?,
    var updated_at: String?
)

data class Detail(
    var created_at: String?,
    var id: Int?,
    var order_id: Int?,
    var price: String?,
    var product: Product?,
    var product_id: Int?,
    var quantity: Int?,
    var updated_at: String?
)

data class Product(
    var category: Category?,
    var category_id: Int?,
    var currency: String?,
    var id: Int?,
    var image: String?,
    var name: String?,
    var price: String?,
    var total_quantity: String?
)

data class Category(
    var id: Int?,
    var name: String?,
    var parent_id: Any?
)