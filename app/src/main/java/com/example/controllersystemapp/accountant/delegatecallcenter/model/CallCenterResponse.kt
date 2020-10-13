package com.example.controllersystemapp.accountant.delegatecallcenter.model

data class CallCenterResponse(
    var `data`: Data?
)

data class Data(
    var count: Int?,
    var list: ArrayList<CallCenterData>?
)

data class CallCenterData(
    var city_id: Int?,
    var email: String?,
    var enable_notification: Int?,
    var id: Int?,
    var image: String?,
    var lat: String?,
    var long: String?,
    var name: String?,
    var phone: String?,
    var role_id: Int?,
    var status: Int?
)