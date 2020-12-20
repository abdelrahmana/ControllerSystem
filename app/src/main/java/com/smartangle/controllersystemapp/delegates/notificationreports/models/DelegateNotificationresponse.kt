package com.smartangle.controllersystemapp.delegates.notificationreports.models

import java.io.Serializable

data class DelegateNotificationresponse (
    val name: String?="",
//    val quantityList : ArrayList<Int>?,
//    val storesIdList : ArrayList<Int>?,
    var isChecked : Boolean = false

): Serializable