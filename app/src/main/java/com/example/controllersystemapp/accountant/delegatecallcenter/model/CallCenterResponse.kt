package com.example.controllersystemapp.accountant.delegatecallcenter.model

data class CallCenterResponse(
    var `data`: Data?
)

data class Data(
    var count: Int?,
    var list: ArrayList<CallCenterDelegateData>?
)

data class CallCenterDelegateData(
    var city_id: Int?=-1,
    var email: String?="",
    var enable_notification: Int?=-1,
    var id: Int?=0,
    var image: String?="",
    var lat: String?="",
    var long: String?="",
    var name: String?="",
    var phone: String?="",
    var role_id: Int?=-1,
    var status: Int?=-1
)