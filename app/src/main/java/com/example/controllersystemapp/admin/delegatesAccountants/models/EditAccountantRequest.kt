package com.example.controllersystemapp.admin.delegatesAccountants.models

data class EditAccountantRequest (

    var id : Int?,
    var name : String?,
    var phone : String?,
    var city_id : Int?,
    var email : String?,
    var status : Int?
)