package com.smartangle.controllersystemapp.admin.reports.model

data class SalesReportResponse(
    var countOrders: Int?,
    var `data`: ArrayList<Data>?,
    var month: Int?,
    var year: Int?
)

data class Data(
    var accountant_id: Any?,
    var address: String?,
    var created_at: String?,
    var created_by: String?,
    var currency: String?,
    var delegate_id: String?,
    var details: List<Detail>?,
    var email: String?,
    var id: Int?,
    var lat: Any?,
    var long: Any?,
    var name: String?,
    var order_number: String?,
    var phone: String?,
    var shipment_cost: String?,
    var status: String?,
    var status_word: String?,
    var total_price: String?,
    var updated_at: String?
)

data class Detail(
    var created_at: String?,
    var id: Int?,
    var order_id: String?,
    var price: String?,
    var product: Product?,
    var product_id: String?,
    var quantity: String?,
    var updated_at: String?
)

data class Product(
    var category: Category?,
    var category_id: String?,
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
    var parent_id: String?
)