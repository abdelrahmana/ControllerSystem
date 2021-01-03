package com.smartangle.controllersystemapp.admin.settings.masrufat

import android.app.Activity
import android.util.Log
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AddAccountantRequest
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.EditAccountantRequest
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object AdminExpensesPresenter {


    fun getAdminExpensesList(
        webService: WebService,
        activity: Activity,
        model: ViewModelHandleChangeFragmentclass
    ) {

        Log.d("testApi", "getData")
        webService.adminExpensesList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<ExpensesListResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<ExpensesListResponse>) {

                    if (response.isSuccessful) {
                        Log.d("testApi", "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    } else {
                        Log.d("testApi", "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }


                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi", "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                }


            })


    }


    fun getAdminExpensesDetails(
        webService: WebService, expensesId: Int, activity: Activity,
        model: ViewModelHandleChangeFragmentclass
    ) {

        Log.d("testApi", "getData")
        webService.adminExpensesDetails(expensesId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<ExpensesDetailsResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<ExpensesDetailsResponse>) {

                    if (response.isSuccessful) {
                        Log.d("testApi", "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    } else {
                        Log.d("testApi", "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }


                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi", "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                }


            })


    }


    fun adminAcceptOrRejectExpenses(
        webService: WebService, expensesId: Int, status: Int,
        activity: Activity, model: ViewModelHandleChangeFragmentclass
    ) {

        val params: LinkedHashMap<String, Any> = LinkedHashMap()
        params["id"] = expensesId
        params["status"] = status // 1 for accept 2 for reject

        webService.adminAcceptOrRejectExpenses(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<SuccessModel>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<SuccessModel>) {

                    if (response.isSuccessful) {
                        Log.d("testApi", "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    } else {
                        Log.d("testApi", "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }


                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi", "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                }


            })


    }
}