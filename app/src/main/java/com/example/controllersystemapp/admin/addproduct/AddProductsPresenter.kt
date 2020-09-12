package com.example.controllersystemapp.admin.addproduct

import android.app.Activity
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat.create
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.controllersystemapp.admin.storesproducts.models.ProductsListResponse
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File


object AddProductsPresenter {

    fun getAddProduct(webService: WebService, addProductRequest: AddProductRequest?, activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {

        var builder = MultipartBody.Builder()

        var bitmapUpdatedImage: Bitmap? = null

        for (i in 0 until addProductRequest?.images!!.size) {
            Glide.with(activity)
                .asBitmap()
                .load(addProductRequest?.images?.get(i) ?: "")
                .into(object : SimpleTarget<Bitmap?>() {

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {

                        bitmapUpdatedImage = resource
                        val f = UtilKotlin.getCreatedFileMultiPartFromBitmap(
                            "image[$i]",
                            bitmapUpdatedImage!!,
                            "jpg",
                            activity!!
                        )

                        builder.addFormDataPart("images[$i]", f.name, RequestBody.create("multipart/form-data".toMediaTypeOrNull(), f))

                        if (i == (addProductRequest?.images?.size ?:0)- 1) // last item
                        {
                            builder.setType(MultipartBody.FORM)
                                .addFormDataPart("name", addProductRequest?.name.toString())
                                .addFormDataPart("description", addProductRequest?.description.toString())
                                .addFormDataPart("price", addProductRequest?.price.toString())
                                .addFormDataPart("barcode", addProductRequest?.barcode.toString())
                                .addFormDataPart("category_id", addProductRequest?.category_id.toString())


                            for (i in 0 until addProductRequest?.warehouse_id!!.size) {

                                builder.addFormDataPart("warehouse_id[$i]",
                                    addProductRequest.warehouse_id!![i].toString()
                                )

                            }

                            for (i in 0 until addProductRequest?.quantity!!.size) {

                                builder.addFormDataPart("quantity[$i]",
                                    addProductRequest.quantity!![i].toString()
                                )

                            }



                            val requestBody = builder.build()

                            Log.d("testApi" , "getData")
                            webService.addProduct(requestBody)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : DisposableObserver<Response<SuccessModel>>() {
                                    override fun onComplete() {

                                        //hideLoader()
                                        //  model.setShowLoader(false)
                                        dispose()

                                    }

                                    override fun onNext(response: Response<SuccessModel>) {

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
                })
        }




    }




}