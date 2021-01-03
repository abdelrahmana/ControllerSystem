package com.smartangle.util.ApiConfiguration


import com.smartangle.controllersystemapp.accountant.delegatecallcenter.debts.AccountantDebtsListResponse
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.*
import com.smartangle.controllersystemapp.accountant.products.models.AccountantProductsListResponse
import com.smartangle.controllersystemapp.accountant.settings.expenses.AccountantExpensesListResponse
import com.smartangle.controllersystemapp.accountant.makeorder.models.AccountantMakeOrderRequest
import com.smartangle.controllersystemapp.accountant.sales.model.ItemListResponses
import com.smartangle.controllersystemapp.accountant.sales.model.SalesResponse
import com.smartangle.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantDetailsResponse
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantListResponse
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AddAccountantRequest
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.EditAccountantRequest
import com.smartangle.controllersystemapp.admin.settings.admin.AdminDetailsResponse
import com.smartangle.controllersystemapp.admin.settings.admin.AdminListResponse
import com.smartangle.controllersystemapp.admin.settings.editpassword.EditPasswordRequest
import com.smartangle.controllersystemapp.admin.specialcustomers.AddClientRequest
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientDetailsResponse
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientsListResponse
import com.smartangle.controllersystemapp.admin.specialcustomers.EditClientRequest
import com.smartangle.controllersystemapp.admin.storesproducts.models.*
import com.smartangle.controllersystemapp.admin.storesproducts.models.AddStoreRequest
import com.smartangle.controllersystemapp.admin.storesproducts.models.ProductsListResponse
import com.smartangle.controllersystemapp.admin.storesproducts.models.StoresListResponse
import com.smartangle.controllersystemapp.callcenter.delegate.model.DelegateResponse
import com.smartangle.controllersystemapp.callcenter.delegate.model.ItemDetailsResponse
import com.smartangle.controllersystemapp.callcenter.delegate.model.ItemListResponse
import com.smartangle.controllersystemapp.callcenter.delegate.model.OrderResponse
import com.smartangle.controllersystemapp.callcenter.maketalbya.model.OrderCreateRequest
import com.smartangle.controllersystemapp.callcenter.maketalbya.productclassification.lastsubcategory.productmodel.ProductResponse
import com.smartangle.controllersystemapp.common.chat.model.RequestMessgae
import com.smartangle.controllersystemapp.common.chat.model.ResponseChatList
import com.smartangle.controllersystemapp.common.cities.CitiesListResponse
import com.smartangle.controllersystemapp.common.forgetpassword.model.RequestModelNewPass
import com.smartangle.controllersystemapp.common.login.LoginRequest
import com.smartangle.controllersystemapp.common.login.LoginResponse
import com.smartangle.controllersystemapp.delegates.makeorder.model.DelegateMakeOrderRequest
import com.smartangle.controllersystemapp.delegates.makeorder.model.DelegateProductsListResponse
import com.smartangle.controllersystemapp.delegates.wallet.models.DelegateOrderItemDetailsResponse
import com.smartangle.controllersystemapp.delegates.wallet.models.DelegateOrderItemsListResponse
import com.smartangle.controllersystemapp.delegates.wallet.models.DelegateOrdersListResponse
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.*
import com.smartangle.controllersystemapp.accountant.products.models.AccountantProdDetailsResponse
import com.smartangle.controllersystemapp.accountant.settings.expenses.AccountantExpensesDetailsResponse
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.model.DelegateListResponse
import com.smartangle.controllersystemapp.admin.settings.masrufat.ExpensesDetailsResponse
import com.smartangle.controllersystemapp.admin.settings.masrufat.ExpensesListResponse

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

    @GET("accountant/orders/items")
    fun getItemListAccountant(@QueryMap map : HashMap<String,Any>): Observable<Response<ItemListResponses>>

    @Headers("Accept: application/json")
    @GET("list/cities")
    fun citiesList(): Observable<Response<CitiesListResponse>>


    @Headers("Accept: application/json")
    @POST("admin/accountants/create")
    fun addAccountant(@Body addAccountantRequest : AddAccountantRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("admin/accountants/edit")
    fun editAccountant(@Body editAccountantRequest : EditAccountantRequest): Observable<Response<SuccessModel>>

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
    @POST("admin/ware-house/edit")
    fun editCurrentStore(@Body addStoreRequest : AddStoreRequest): Observable<Response<SuccessModel>>

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
    @POST("admin/admin/edit")
    fun editAdmin(@Body editAccountantRequest: EditAccountantRequest): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @GET("admin/clients/list")
    fun clientsList(@QueryMap hashMap: HashMap<String, Any>?): Observable<Response<ClientsListResponse>>

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
    @POST("admin/clients/edit")
    fun editClient(@Body editClientRequest : EditClientRequest): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @DELETE("admin/categories/delete")
    fun deleteCategory(@Query("id") categoryId : Int): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("auth/password/reset")
   suspend fun setNewPassword(@Body requestNewPass : RequestModelNewPass): Response<SuccessModel>


    @Headers("Accept: application/json")
    @POST("auth/logout")
    fun loginOut(): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @GET("admin/expenses/list")
    fun adminExpensesList(): Observable<Response<ExpensesListResponse>>

    @Headers("Accept: application/json")
    @GET("admin/expenses/details")
    fun adminExpensesDetails(@Query("id") expensesId : Int?): Observable<Response<ExpensesDetailsResponse>>

    @Headers("Accept: application/json")
    @POST("admin/expenses/update-accountant-expense")
    fun adminAcceptOrRejectExpenses(@Body hashMap: HashMap<String,Any>?): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @GET("accountant/delegates/details")
    fun adminDelegateDetails(@Query("id") delegateId : Int?): Observable<Response<AccountantDelegateDetails>>


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

    @Headers("Accept: application/json")
    @GET("accountant/delegates/details")
    fun accountantDelegateDetails(@Query("id") delegateId : Int?): Observable<Response<AccountantDelegateDetails>>

    @Headers("Accept: application/json")
    @GET("accountant/orders/items")
    fun accDelegateOrderItems(@Query("order_id") order_id : Int?): Observable<Response<AccDelegateOrderItems>>

    @Headers("Accept: application/json")
    @GET("accountant/orders/item-details")
    fun accDelegateOrderItemsDetails(@Query("item_id") order_id : Int?): Observable<Response<AccDelegateOrderItemsDetails>>


    @Headers("Accept: application/json")
    @DELETE("accountant/delegates/delete")
    fun accountantDeleteDelegate(@Query("id") delegateId : Int): Observable<Response<SuccessModel>>


    @Headers("Accept: application/json")
    @GET("accountant/debts/list")
    fun accountantDebtsList(@Query("page") page : Int? , @Query("delegate_id") delegate_id : Int?):
            Observable<Response<AccountantDebtsListResponse>>


    @Headers("Accept: application/json")
    @POST("accountant/debts/update")
    fun accountantUpdateDebt(@Body hashMap: HashMap<String,Any>?): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @DELETE("accountant/debts/delete")
    fun accountantDeleteDebts(@Query("id") delegateId : Int): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @POST("accountant/debts/create")
    fun accountantCreateDebts(@Body hashMap: HashMap<String,Any>?): Observable<Response<SuccessModel>>

    @Headers("Accept: application/json")
    @GET("accountant/orders/list")
   suspend fun getSalesList(@Query("status") statusId : Int): Response<SalesResponse>
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

    @GET("accountant/orders/item-details")
    fun getAccountantItemDetails(@QueryMap map : HashMap<String,Any>): Observable<Response<ItemDetailsResponse>>

    @POST("chat/send")
    fun requestPostMessage(@Body requestBody : RequestMessgae): Observable<Response<SuccessModel>>

    @GET("chat/list")
    fun getMessagesList(@Query("receiver_id")id:Int): Observable<Response<ResponseChatList>>

    @Headers("Accept: application/json")
    @GET("admin/delegates/list")
    fun getDelegatesAdmin(@Query("page") page : Int): Observable<Response<DelegateListResponse>>
}