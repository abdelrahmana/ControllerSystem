package com.example.util.ApiConfiguration
 // this is already created
data class ErrorBodyResponse(
         var msg: ArrayList<String> = ArrayList(),
         var message : String? = "",
         var status: Boolean = false // false
)