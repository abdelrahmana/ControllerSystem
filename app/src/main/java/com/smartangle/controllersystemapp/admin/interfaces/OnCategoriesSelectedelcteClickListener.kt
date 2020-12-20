package com.smartangle.controllersystemapp.admin.interfaces

import com.smartangle.controllersystemapp.admin.categories.models.Data

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