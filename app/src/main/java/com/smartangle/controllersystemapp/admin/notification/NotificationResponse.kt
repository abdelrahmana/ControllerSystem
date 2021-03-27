package com.smartangle.controllersystemapp.admin.notification

data class NotificationResponse(
    var count_un_seen: Int?,
    var `data`: ArrayList<Data>?
)

data class Data(
    var created_at: String?,
    var id: Int?,
    var receiver_id: String?,
    var sender_id: String?,
    var status: String?,
    var text: String?,
    var title: String?,
    var type: String?,
    var updated_at: String?
)