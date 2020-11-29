package com.example.controllersystemapp.callcenter.delegate.model

data class OrderResponse(
    var `data`: Dataa?
)

data class Dataa(
    var city: City?,
    var city_id: Int?,
    var delegate_orders: ArrayList<DelegateOrder>?,
    var email: String?,
    var enable_notification: Int?,
    var id: Int?,
    var image: String?,
    var lat: Any?,
    var long: Any?,
    var name: String?,
    var phone: String?,
    var role_id: Int?,
    var status: Int?,
    var warehouse_id: Int?
)

data class City(
    var id: Int?,
    var name: String?
)

data class DelegateOrder(
    var address: String?,
    var created_at: String?,
    var created_by: Int?,
    var currency: String?,
    var delegate_id: Int?,
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