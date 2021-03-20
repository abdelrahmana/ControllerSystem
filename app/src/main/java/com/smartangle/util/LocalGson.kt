package com.smartangle.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientDetailsResponse
import com.smartangle.controllersystemapp.common.notification.MessageSender
import com.smartangle.controllersystemapp.common.notification.Sender

object LocalGson {

    fun getMessageSenderModel(gsonString: String) : MessageSender?{
        val gson = Gson()
        val typeToken = object : TypeToken<MessageSender?>() {}.type
        val obj = gson.fromJson<MessageSender>(gsonString, typeToken) ?: MessageSender() //ResponseLogin(Data("", null))
        return obj
    }

}