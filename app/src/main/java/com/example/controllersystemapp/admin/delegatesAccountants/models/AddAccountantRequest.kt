package com.example.controllersystemapp.admin.delegatesAccountants.models

data class AddAccountantRequest (

    var name : String?,
    var phone : String?,
    var password : String?,
    var password_confirmation : String?,
    var role_id : Int?,
    var city_id : Int?
)