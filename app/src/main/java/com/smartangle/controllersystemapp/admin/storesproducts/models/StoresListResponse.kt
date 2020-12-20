package com.smartangle.controllersystemapp.admin.storesproducts.models

import java.io.Serializable

data class StoresListResponse(
    val `data`: List<StoresData>?
)

data class StoresData(
    val accountant: Accountant?,
    val accountant_id: String?,
    val address: String?,
    val id: Int?,
    val name: String?,
//    val quantityList : ArrayList<Int>?,
//    val storesIdList : ArrayList<Int>?,
    var isChecked : Boolean = false,
    var quantity: Int?=1



    ): Serializable

data class Accountant(
    val id: Int?,
    val image: String?,
    val name: String?,
    val phone: String?
)