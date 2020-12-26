package com.smartangle.controllersystemapp.admin.storesproducts.models

data class AddStoreRequest (
    var id  : Int= -1,
    var name : String?,
    var address : String?,
    var accountant_id : Int?,
    var category_id : ArrayList<Int>?
    )