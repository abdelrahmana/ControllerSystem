package com.example.controllersystemapp.callcenter.delegate.model

import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData

data class DelegateResponse(
    var `data`: Data?
)

data class Data(
    var count: Int?,
    var has_more_page: Boolean?,
    var list: ArrayList<CallCenterDelegateData>?
)

/*data class Delegate(
    var city_id: Int?,
    var email: String?,
    var enable_notification: Int?,
    var id: Int?,
    var image: String?,
    var lat: Any?,
    var long: Any?,
    var name: String?,
    var phone: String?,
    var role_id: Int?,
    var status: Int?
)*/
