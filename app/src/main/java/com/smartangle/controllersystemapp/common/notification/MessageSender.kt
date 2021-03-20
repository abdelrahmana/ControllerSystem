package com.smartangle.controllersystemapp.common.notification

import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData

data class MessageSender(
    var message_information: MessageInformation?=null,
    var type_notification: String?=""
)

data class MessageInformation(
    var created_at: String?,
    var id: Int?,
    var message: String?,
    var `receiver`: Receiver?,
    var receiver_id: String?,
    var sender: CallCenterDelegateData?,
    var sender_id: String?,
    var type: String?,
    var type_notification: String?,
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