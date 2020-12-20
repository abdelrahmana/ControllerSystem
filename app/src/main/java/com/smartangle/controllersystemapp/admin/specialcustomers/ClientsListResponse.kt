package com.smartangle.controllersystemapp.admin.specialcustomers

data class ClientsListResponse(
    val `data`: Data
)

data class Data(
    val count: Int?,
    val list: List<ClientsData>?
)

data class ClientsData(
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