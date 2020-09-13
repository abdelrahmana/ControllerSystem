package com.example.controllersystemapp.admin.storesproducts.models

data class AddStoreRequest (

    var name : String?,
    var address : String?,
    var accountant_id : Int?,
    var category_id : ArrayList<Int>?
    )