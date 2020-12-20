package com.smartangle.controllersystemapp.accountant.makeorder.models

data class AccountantMakeOrderRequest(

    var name: String?,
    var email: String?,
    var phone: String?,
    var address: String?,
    var shipment_cost: String?,
    var delegate_id: Int?,
    var products : ArrayList<Int>?,
    var quantity : ArrayList<Int>?

)