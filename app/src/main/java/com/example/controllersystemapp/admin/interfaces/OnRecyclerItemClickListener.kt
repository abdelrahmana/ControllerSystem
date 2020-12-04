package com.example.controllersystemapp.admin.interfaces

interface OnRecyclerItemClickListener {

    fun onItemClick(position : Int)
    fun delegateClickListener(position: Int) {

    }

    fun adminOptionsClickListener(position: Int) {

    }

    fun acceptOrderClickListener(position: Int) {

    }

}