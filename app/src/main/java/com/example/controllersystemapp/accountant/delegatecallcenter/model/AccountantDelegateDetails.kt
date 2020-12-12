package com.example.controllersystemapp.accountant.delegatecallcenter.model

data class AccountantDelegateDetails(
    val `data`: DataAccDelegateDetails?
)

data class DataAccDelegateDetails(
    val city: City?,
    val city_id: String?,
    val created_by: String?,
    val delegate_orders: List<DelegateOrder>?,
    val email: String?,
    val enable_notification: String?,
    val id: Int?,
    val image: String?,
    val lat: Any?,
    val long: Any?,
    val name: String?,
    val phone: String?,
    val role_id: String?,
    val status: String?,
    val warehouse_id: Int?
)

data class City(
    val id: Int?,
    val name: String?
)

data class DelegateOrder(
    val address: String?,
    val created_at: String?,
    val created_by: String?,
    val currency: String?,
    val delegate_id: String?,
    val email: String?,
    val id: Int?,
    val name: String?,
    val order_number: String?,
    val phone: String?,
    val shipment_cost: String?,
    val status: String?,
    val status_word: String?,
    val total_price: String?,
    val updated_at: String?
)