package com.smartangle.controllersystemapp.admin.storesproducts.models

data class StoreDetailsResponse(
    val `data`: DataStoreDetails?
)

data class DataStoreDetails(
    val accountant: AccountantStoreDetails?,
    val accountant_id: String?,
    val address: String?,
    val categories: List<CategoryStoreDetails>?,
    val id: Int?,
    val name: String?
)

data class AccountantStoreDetails(
    val email: Any?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val phone: String?,
    val warehouse_id: Any?
)

data class CategoryStoreDetails(
    val id: Int?,
    val name: String?,
    val parent_id: Any?,
    val pivot: PivotStoreDetails?
)

data class PivotStoreDetails(
    val category_id: String?,
    val warehouse_id: String?
)