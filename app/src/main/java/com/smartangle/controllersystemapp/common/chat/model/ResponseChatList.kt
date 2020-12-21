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
    var receiver_id: Int?,
    var sender: Sender?,
    var sender_id: Int?,
    var type: String?, // if type == sender it's related to me  else if type is reciever it's related to the other person who sent the text
    var updated_at: String?
)

data class Receiver( // may be me or other person
    var id: Int?,
    var image: String?,
    var name: String?,
    var warehouse_id: Any?
)

data class Sender( // may be me or other person
    var id: Int?,
    var image: String?,
    var name: String?,
    var warehouse_id: Any?
)