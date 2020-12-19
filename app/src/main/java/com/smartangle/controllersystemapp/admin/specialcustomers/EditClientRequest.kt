package com.smartangle.controllersystemapp.admin.specialcustomers

data class EditClientRequest(

    var id : Int?,
    var name : String?,
    var phone : String?,
    var address : String?,
    var email : String?
)