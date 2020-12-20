package com.smartangle.controllersystemapp.callcenter.maketalbya.model

data class OrderCreateRequest(
    var address: String?="",
    var delegate_id: Int?=0,
    var email: String?="",
    var name: String?="",
    var phone: String?="",
    var products: ArrayList<Int>?= ArrayList(),
    var quantity: ArrayList<Int>?=ArrayList(),
    var shipment_cost: String?=""
)