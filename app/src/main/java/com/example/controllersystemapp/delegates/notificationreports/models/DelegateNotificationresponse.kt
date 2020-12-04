package com.example.controllersystemapp.delegates.notificationreports.models

import com.example.controllersystemapp.admin.storesproducts.models.Accountant
import java.io.Serializable

data class DelegateNotificationresponse (
    val name: String?="",
//    val quantityList : ArrayList<Int>?,
//    val storesIdList : ArrayList<Int>?,
    var isChecked : Boolean = false

): Serializable