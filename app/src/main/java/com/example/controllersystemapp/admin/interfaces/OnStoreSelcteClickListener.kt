package com.example.controllersystemapp.admin.interfaces

interface OnStoreSelcteClickListener {

    fun onClickItemClick(
        position: Int,
        quantityList: ArrayList<Int>,
        storesIdList: ArrayList<Int>
    )
    fun onIncreaseItemClick(position: Int, text: String)
    fun onDecreaseItemClick(position: Int, text: String)





}