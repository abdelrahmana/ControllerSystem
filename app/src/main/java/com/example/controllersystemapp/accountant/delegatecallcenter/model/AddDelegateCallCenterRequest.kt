package com.example.controllersystemapp.accountant.delegatecallcenter.model

data class AddDelegateCallCenterRequest(
    var name : String?="",
    var city_id: Int?=-1,
    var password: String?="",
    var password_confirmation: String? ="",
    var phone: String?="",
    var role_id: Int?=-1,
    var email : String?="",
    var status : String?="",
    var id : Int?=0
)