package com.example.controllersystemapp.admin.storesproducts.models

data class AddStoreRequest (
    var id  : Int= -1,
    var name : String?="",
    var address : String?="",
    var accountant_id : Int?=-1,
    var category_id : ArrayList<Int>?= ArrayList()
)
