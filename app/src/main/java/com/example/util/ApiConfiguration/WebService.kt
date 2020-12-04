package com.example.util.ApiConfiguration


import com.example.controllersystemapp.accountant.products.models.AccountantProdDetailsResponse
import com.example.controllersystemapp.accountant.products.models.AccountantProductsListResponse
import com.example.controllersystemapp.accountant.settings.expenses.AccountantExpensesDetailsResponse
import com.example.controllersystemapp.accountant.settings.expenses.AccountantExpensesListResponse
import com.example.controllersystemapp.accountant.delegatecallcenter.model.AddDelegateCallCenterRequest
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.example.controllersystemapp.accountant.makeorder.models.AccountantMakeOrderRequest
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantDetailsResponse
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantListResponse
import com.example.controllersystemapp.admin.delegatesAccountants.models.AddAccountantRequest
import com.example.controllersystemapp.admin.settings.admin.AdminDetailsResponse
import com.example.controllersystemapp.admin.settings.admin.AdminListResponse
import com.example.controllersystemapp.admin.settings.editpassword.EditPasswordRequest
import com.example.controllersystemapp.admin.specialcustomers.AddClientRequest
import com.example.controllersystemapp.admin.specialcustomers.ClientDetailsResponse
import com.example.controllersystemapp.admin.specialcustomers.ClientsListResponse
import com.example.controllersystemapp.admin.storesproducts.models.*
import com.example.controllersystemapp.admin.storesproducts.models.AddStoreRequest
import com.example.controllersystemapp.admin.storesproducts.models.ProductsListResponse
import com.example.controllersystemapp.admin.storesproducts.models.StoresListResponse
import com.example.controllersystemapp.callcenter.delegate.model.DelegateResponse
import com.example.controllersystemapp.callcenter.delegate.model.ItemDetailsResponse
import com.example.controllersystemapp.callcenter.delegate.model.ItemListResponse
import com.example.controllersystemapp.callcenter.delegate.model.OrderResponse
import com.example.controllersystemapp.callcenter.maketalbya.model.OrderCreateRequest
import com.example.controllersystemapp.callcenter.maketalbya.productclassification.lastsubcategory.productmodel.ProductResponse
import com.example.controllersystemapp.common.cities.CitiesListResponse
import com.example.controllersystemapp.common.forgetpassword.model.RequestModelNewPass
import com.example.controllersystemapp.common.login.LoginRequest
import com.example.controllersystemapp.common.login.LoginResponse
import com.example.controllersystemapp.delegates.makeorder.model.DelegateMakeOrderRequest
import com.example.controllersystemapp.delegates.makeorder.model.DelegateProductsListResponse
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrderItemDetailsResponse
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrderItemsListResponse
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrdersListResponse
import io.reactivex.Observable
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
    @GET("admin/products/details")
    fun productDetails(@Query("id") productId : Int?): Observable<Response<ProductsDetailsResponse>>

    @Headers("Accept: application/json")
    @POST("admin/products/create")
    fun addProduct(@Body requestBody : RequestBody): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("admin/products/edit")
    fun editProduct(@Body requestBody : RequestBody): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @GET("admin/categories/list")
    fun categoriesList(@Query("parent_id") parentId : Int?): Observable<Response<CategoriesListResponse>>

    @Headers("Accept: application/json")
    @POST("admin/categories/create")
    fun addCategory(@Body hashMap: HashMap<String,Any>?): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("admin/categories/edit")
    fun editCategory(@Body hashMap: HashMap<String,Any>?): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @GET("admin/ware-house/list")
    fun storesList(@Query("category_id") categoryId : Int?): Observable<Response<StoresListResponse>>

    @Headers("Accept: application/json")
    @GET("admin/ware-house/details")
    fun storeDetails(@Query("id") storeId : Int?): Observable<Response<StoreDetailsResponse>>



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


    @Headers("Accept: application/json")
    @GET("admin/admin/list")
    fun adminsList(): Observable<Response<AdminListResponse>>

    @Headers("Accept: application/json")
    @GET("admin/admin/details")
    fun adminDetails(@Query("id") adminId : Int?): Observable<Response<AdminDetailsResponse>>


    @Headers("Accept: application/json")
    @DELETE("admin/admin/delete")
    fun deleteAdmin(@Query("id") adminId : Int): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @POST("admin/admin/create")
    fun addAdmin(@Body addAccountantRequest : AddAccountantRequest): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @GET("admin/clients/list")
    fun clientsList(): Observable<Response<ClientsListResponse>>

    @Headers("Accept: application/json")
    @GET("admin/clients/details")
    fun clientDetails(@Query("id") clientId : Int?): Observable<Response<ClientDetailsResponse>>



    @Headers("Accept: application/json")
    @DELETE("admin/clients/delete")
    fun deleteClient(@Query("id") clientId : Int): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @POST("admin/clients/create")
    fun addClient(@Body addClientRequest : AddClientRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @DELETE("admin/categories/delete")
    fun deleteCategory(@Query("id") categoryId : Int): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("auth/password/reset")
   suspend fun setNewPassword(@Body requestNewPass : RequestModelNewPass): Response<SuccessModel>


    @Headers("Accept: application/json")
    @POST("auth/logout")
    fun loginOut(): Observable<Response<SuccessModel>>


    //Accountant Data

//    @Headers("Accept: application/json")
//    @GET("accountant/products/list")
//    fun accountantProductsList(): Observable<Response<AccountantProductsListResponse>>

    @Headers("Accept: application/json")
    @GET("admin/products/details")
    fun accProductDetails(@Query("id") productId : Int?): Observable<Response<AccountantProdDetailsResponse>>

    @Headers("Accept: application/json")
    @GET("accountant/call-center/list")
    fun getCallCenterList(): Observable<Response<CallCenterResponse>>
    @Headers("Accept: application/json")
    @GET("accountant/delegates/list")
    fun getDelegates(): Observable<Response<CallCenterResponse>>

    @Headers("Accept: application/json")
    @POST("accountant/call-center/create")
    fun addCallCenter(@Body callCenterRequest: AddDelegateCallCenterRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("accountant/delegates/create")
    fun addDelegate(@Body callCenterRequest: AddDelegateCallCenterRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("accountant/call-center/edit")
    fun editCallCenter(@Body callCenterRequest: AddDelegateCallCenterRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("accountant/delegates/edit")
    fun editDelegate(@Body callCenterRequest: AddDelegateCallCenterRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("users/update-password")
    fun changePassword(@Body callCenterRequest: EditPasswordRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @GET("accountant/expenses/list")
    fun accountantExpensesList(): Observable<Response<AccountantExpensesListResponse>>


    @Headers("Accept: application/json")
    @POST("accountant/expenses/create")
    fun accountantCreateExpenses(@Body hashMap: HashMap<String,Any>?): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @DELETE("accountant/expenses/delete")
    fun deleteAccountantExpenses(@Query("id") expensesId : Int): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @GET("accountant/expenses/details")
    fun accountantExpensesDetails(@Query("id") expensesId : Int): Observable<Response<AccountantExpensesDetailsResponse>>


    @Headers("Accept: application/json")
    @POST("accountant/expenses/edit")
    fun accountantEditExpenses(@Body hashMap: HashMap<String,Any>?): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @GET("accountant/categories/list")
    fun accountantCategoriesList(@Query("parent_id") parentId : Int? , @Query("name") name : String?):
            Observable<Response<CategoriesListResponse>>

    @Headers("Accept: application/json")
    @GET("accountant/products/list")
    fun accountantProductsList(@Query("category_id") categoryId : Int? , @Query("name") name : String?):
            Observable<Response<AccountantProductsListResponse>>


    @Headers("Accept: application/json")
    @POST("accountant/orders/create")
    fun accountantCreateOrder(@Body accountantMakeOrderRequest: AccountantMakeOrderRequest): Observable<Response<SuccessModel>>


    //Delegates Api

    @Headers("Accept: application/json")
    @GET("delegate/orders/list")
    fun delegateOrdersList(@Query("status") status : Int): Observable<Response<DelegateOrdersListResponse>>


    @Headers("Accept: application/json")
    @GET("delegate/orders/items")
    fun delegateOrderItemsList(@Query("order_id") order_id : Int): Observable<Response<DelegateOrderItemsListResponse>>

    @Headers("Accept: application/json")
    @GET("delegate/orders/item-details")
    fun delegateOrderItemDetails(@Query("item_id") item_id : Int): Observable<Response<DelegateOrderItemDetailsResponse>>

    @Headers("Accept: application/json")
    @POST("delegate/orders/update-status")
    fun delegateUpdateOrderStatus(@Body hashMap: HashMap<String,Any>?): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("delegate/orders/create")
    fun delegateCreateOrder(@Body delegateMakeOrderRequest: DelegateMakeOrderRequest): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @GET("delegate/categories/list")
    fun delegateCategoriesList(@Query("parent_id") parentId : Int? , @Query("name") name : String?):
            Observable<Response<CategoriesListResponse>>

    @Headers("Accept: application/json")
    @GET("delegate/products/list")
    fun delegateProductsList(@Query("category_id") categoryId : Int? , @Query("name") name : String?):
            Observable<Response<DelegateProductsListResponse>>

    @GET("call-center/delegates/list")
    fun getDelegateListInCallCenter(@QueryMap mapPage : HashMap<String,Any>): Observable<Response<DelegateResponse>>

    @GET("call-center/delegates/details")
    fun getOrderList(@QueryMap map : HashMap<String,Any>): Observable<Response<OrderResponse>>

    @GET("call-center/orders/items")
    fun getItemList(@QueryMap map : HashMap<String,Any>): Observable<Response<ItemListResponse>>

    @GET("call-center/orders/item-details")
    fun getItemDetailsData(@QueryMap map : HashMap<String,Any>): Observable<Response<ItemDetailsResponse>>


    @Headers("Accept: application/json")
    @GET("call-center/categories/list")
    fun categoriesListCallCenter(@Query("parent_id") parentId : Int?): Observable<Response<CategoriesListResponse>>

    @Headers("Accept: application/json")
    @GET("call-center/products/list")
    fun getCategoryId(@Query("category_id") categoryId : Int?): Observable<Response<ProductResponse>>


    @Headers("Accept: application/json")
    @POST("call-center/orders/create")
    fun postOrder(@Body orderCreateRequest: OrderCreateRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @DELETE("admin/products/delete-images")
    fun deleteImage(@Query("id[]") array :Array<Int>): Observable<Response<SuccessModel>>
    @Headers("Accept: application/json")
    // @Multipart
    @POST("users/update-profile")
    fun editProfileWebService(@Body requestBody : RequestBody): Observable<Response<LoginResponse>>
}