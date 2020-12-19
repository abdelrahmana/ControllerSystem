package com.smartangle.controllersystemapp.common.cities

data class CitiesListResponse(
    val `data`: Data?
)

data class Data(
    val list: List<Cities>?
)

data class Cities(
    val id: Int?,
    val name: String?
)