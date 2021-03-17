package com.smartangle.util.ApiConfiguration

data class SuccessModelV2(
    val data :Datav?=null,
    val msg: List<String>?
)
class Datav (
    val msg: List<String>?)