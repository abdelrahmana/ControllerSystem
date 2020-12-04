package com.example.controllersystemapp.admin.interfaces

import com.example.controllersystemapp.admin.categories.models.Data

interface OnCategoriesSelectedelcteClickListener {

    fun onClickItemClick(
        position: Int,
        categoriesList: ArrayList<Int>,
        categoriesListNames: ArrayList<String>
    ){

    }


    fun onItemClickData(
        position: Int,
        categoriesList: ArrayList<Data>)



}