package com.example.util.ApiConfiguration


import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantDetailsResponse
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantListResponse
import com.example.controllersystemapp.admin.delegatesAccountants.models.AddAccountantRequest
import com.example.controllersystemapp.admin.storesproducts.models.AddStoreRequest
import com.example.controllersystemapp.admin.storesproducts.models.ProductsListResponse
import com.example.controllersystemapp.admin.storesproducts.models.StoresListResponse
import com.example.controllersystemapp.common.cities.CitiesListResponse
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


    @Headers("Accept: application/json")
    @GET("admin/categories/list")
    fun categoriesList(@Query("parent_id") parentId : Int?): Observable<Response<CategoriesListResponse>>


    @Headers("Accept: application/json")
    @GET("admin/ware-house/list")
    fun storesList(@Query("category_id") categoryId : Int?): Observable<Response<StoresListResponse>>

    @Headers("Accept: application/json")
    @DELETE("admin/products/delete")
    fun deleteProduct(@Query("id") productId : Int , @Query("warehouse_id") warehouseId : Int?):
            Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @GET("admin/accountants/list")
    fun accountantsList(): Observable<Response<AccountantListResponse>>


    @Headers("Accept: application/json")
    @GET("list/cities")
    fun citiesList(): Observable<Response<CitiesListResponse>>


    @Headers("Accept: application/json")
    @POST("admin/accountants/create")
    fun addAccountant(@Body addAccountantRequest : AddAccountantRequest): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @DELETE("admin/accountants/delete")
    fun deleteAccountant(@Query("id") accountantId : Int): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @GET("admin/accountants/details")
    fun accountantDetails(@Query("id") accountantId : Int): Observable<Response<AccountantDetailsResponse>>

    @Headers("Accept: application/json")
    @POST("admin/ware-house/create")
    fun addStore(@Body addStoreRequest : AddStoreRequest): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @DELETE("admin/ware-house/delete")
    fun deleteStore(@Query("id") productId : Int): Observable<Response<SuccessModel>>


}