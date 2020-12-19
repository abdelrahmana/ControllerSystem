package com.smartangle.controllersystemapp.callcenter.delegate.model

data class ItemDetailsResponse(
    var `data`: DataBeans?
)

data class DataBeans(
    var created_at: String?="",
    var id: Int?=0,
    var order: Order?=Order(),
    var order_id: Int?=-1,
    var price: String?="",
    var product: Product?= Product(),
    var product_id: Int?=-1,
    var quantity: Int?=-1,
    var updated_at: String?=""
)

data class Order(
    var address: String?="",
    var created_at: String?="",
    var created_by: Int?=-1,
    var currency: String?="",
    var delegate_id: Int?=-1,
    var email: String?="",
    var id: Int?=-1,
    var name: String?="",
    var order_number: Int?=-1,
    var phone: String?="",
    var shipment_cost: String?="",
    var status: Int?=-1,
    var status_word: String?="",
    var total_price: String?="",
    var updated_at: String?=""
)

data class Product(
    var barcode: String?="",
    var category: Categorys?= Categorys(),
    var category_id: Int?=-1,
    var currency: String?="",
    var description: String?="",
    var id: Int?=-1,
    var image: String?="",
    var name: String?="",
    var total_quantity: String?=""
)

data class Categorys(
    var id: Int?=-1,
    var name: String?="",
    var parent_id: Int?=-1
)