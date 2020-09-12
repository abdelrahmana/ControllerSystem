package com.example.controllersystemapp.admin.addproduct

data class AddProductRequest(
    var name : String?,
    var description : String?,
    var price : String?,
    var barcode : String?,
    var category_id : Int?,
    var quantity : ArrayList<Int>?,
    var warehouse_id : ArrayList<Int>?,
    var images : ArrayList<String>?




    )