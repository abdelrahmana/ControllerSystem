package com.example.controllersystemapp.common.cities

import android.app.Activity
import android.util.Log
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantListResponse
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

object CitiesPresenter {


    fun getCitiesList(webService: WebService, activity: Activity, commonCallsInterface: CommonCallsInterface)
    {

        Log.d("testApi" , "getData")
        webService.citiesList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<CitiesListResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<CitiesListResponse>) {

                    if (response.isSuccessful)
                    {
                        Log.d("testApi" , "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        //model.responseCodeDataSetter(response?.body())
                        commonCallsInterface.getCitiesList(response?.body()!!)

                    }
                    else{
                        Log.d("testApi" , "responseError")
                        //model.setShowLoader(false)
                        //model.onError(response.errorBody())
                        commonCallsInterface.setError(response.errorBody()!!)
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


    interface CommonCallsInterface {  // used in bottom sheet
        fun setError(error : ResponseBody)
        fun getCitiesList(cityResponse: CitiesListResponse) // get cities list
    }
}