package com.example.controllersystemapp.admin.addproduct

data class AddProductRequest(
    var name : String?="",
    var description : String?="",
    var price : String?="",
    var barcode : String?="",
    var category_id : Int?=1,
    var quantity : ArrayList<Int>?= ArrayList(),
    var warehouse_id : ArrayList<Int>?= ArrayList(),
    var images : ArrayList<String>?=ArrayList()

    )