package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.model

data class DelegateListResponse(
    var `data`: Data?
)

data class Data(
    var count: Int?,
    var list: ArrayList<DataBean>?
)

data class List(
    var current_page: Int?,
    var `data`: ArrayList<DataBean>?,
    var first_page_url: String?,
    var from: Int?,
    var last_page: Int?,
    var last_page_url: String?,
    var next_page_url: Any?,
    var path: String?,
    var per_page: Int?,
    var prev_page_url: Any?,
    var to: Int?,
    var total: Int?
)

data class DataBean(
    var city_id: Int?,
    var created_by: Int?,
    var email: String?,
    var enable_notification: Int?,
    var id: Int?,
    var image: String?,
    var lat: Any?,
    var long: Any?,
    var name: String?,
    var phone: String?,
    var role_id: Int?,
    var status: Int?,
    var warehouse_id: Int?
)