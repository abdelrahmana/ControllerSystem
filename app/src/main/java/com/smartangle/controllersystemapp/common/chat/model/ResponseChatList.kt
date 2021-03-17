package com.smartangle.controllersystemapp.common.chat.model

data class ResponseChatList(
    var `data`: Data?
)

data class Data(
    var messages: ArrayList<Message>?
)

data class Message(
    var created_at: String?,
    var id: Int?,
    var message: String?,
    var `receiver`: Receiver?,
    var receiver_id: String?,
    var sender: Sender?,
    var sender_id: String?,
    var type: String?,
    var updated_at: String?
)

data class Receiver(
    var id: Int?,
    var image: String?,
    var name: String?,
    var warehouse_id: Any?
)

data class Sender(
    var id: Int?,
    var image: String?,
    var name: String?,
    var warehouse_id: Any?
)