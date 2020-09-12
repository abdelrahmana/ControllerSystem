package com.example.util.ApiConfiguration


import com.example.controllersystemapp.admin.storesproducts.models.ProductsListResponse
import com.example.controllersystemapp.common.login.LoginRequest
import com.example.controllersystemapp.common.login.LoginResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface WebService {


    @Headers("Accept: application/json")
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Observable<Response<LoginResponse>>


    @Headers("Accept: application/json")
    @GET("admin/products/list")
    fun productsList(@Query("category_id") categoryId : Int?): Observable<Response<ProductsListResponse>>



    @Headers("Accept: application/json")
    @POST("admin/products/create")
    fun addProduct(@Body requestBody : RequestBody): Observable<Response<SuccessModel>>

}