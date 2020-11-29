package com.example.controllersystemapp.callcenter.maketalbya

import android.app.Activity
import android.util.Log
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.storesproducts.models.ProductsListResponse
import com.example.controllersystemapp.callcenter.maketalbya.productclassification.lastsubcategory.productmodel.ProductResponse
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object CategoriesPresenterCallCenter {


    fun getCategoriesList(webService: WebService, parentID : Int?, activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {

        webService.categoriesListCallCenter(parentID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<CategoriesListResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<CategoriesListResponse>) {

                    if (response.isSuccessful)
                    {
                        Log.d("testApi" , "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    }
                    else{
                        Log.d("testApi" , "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }



                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi" , "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!! , e.message.toString())
                }


            })



    }


    fun getProductsList(webService: WebService, parentID : Int?, activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {

        webService.getCategoryId(parentID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<ProductResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<ProductResponse>) {

                    if (response.isSuccessful)
                    {
                        Log.d("testApi" , "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    }
                    else{
                        Log.d("testApi" , "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }



                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi" , "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!! , e.message.toString())
                }


            })



    }

}