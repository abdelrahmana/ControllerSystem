package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates

import android.app.Activity
import android.util.Log
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.AccountantDelegateDetails
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.model.DelegateListResponse
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object AdminDelegatePresenter {
    fun getDelegatesInAdmin(webService: WebService, delegateObserver: DisposableObserver<Response<DelegateListResponse>>,pageNumber : Int)
    {

        webService.getDelegatesAdmin(pageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(delegateObserver)



    }


    fun adminDelegateDetails(webService: WebService, delegateId : Int?, activity: Activity,
                                  model: ViewModelHandleChangeFragmentclass
    )
    {

        Log.d("testApi" , "getData")
        webService.adminDelegateDetails(delegateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<AccountantDelegateDetails>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<AccountantDelegateDetails>) {

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