package com.smartangle.controllersystemapp.accountant.delegatecallcenter.model

data class CallCenterResponse(
    var `data`: Data?
)

data class Data(
    var count: Int?,
    var list: ArrayList<CallCenterDelegateData>?
)

data class CallCenterDelegateData(
    var city_id: String?="",
    var email: String?="",
    var enable_notification: String?="",
    var id: Int?=0,
    var image: String?="",
    var lat: String?="",
    var long: String?="",
    var name: String?="",
    var phone: String?="",
    var created_by: String?="",
    var role_id: String?="",
    var status: String?=""
)