package com.smartangle.controllersystemapp.admin.specialcustomers

data class ClientDetailsResponse(
    val `data`: DataClientDetails?
)

data class DataClientDetails(
    val address: String?,
    val creditor_amount: Int?,
    val currency: String?,
    val email: String?,
    val id: Int?,
    val name: String?,
    val phone: String?,
    val receipt_amount: Int?,
    val total_amount: Int?
)