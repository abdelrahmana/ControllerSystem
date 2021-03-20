package com.smartangle.controllersystemapp.admin.makeorders.model

data class OrderCreateRequestAdmin(
    var delegate_id: Int?=0,
    var client_id : Int?=0,
    var warehouse_id : Int?=0,
  //  var accountant_id : Int?=0,
    var products: ArrayList<Int>?= ArrayList(),
    var quantity: ArrayList<Int>?=ArrayList(),
   // var products: IntArray = IntArray(1),
   // var quantity: IntArray =IntArray(1) ,
    var shipment_cost: String?=""
)