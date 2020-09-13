package com.example.controllersystemapp.admin.categories.models

data class CategoriesListResponse(
    val `data`: List<Data>?
)

data class Data(
    val id: Int?,
    val name: String?,
    val parent: Parent?,
    val parent_id: String?,
    val products_count: String?
)

data class Parent(
    val id: Int?,
    val name: String?
)