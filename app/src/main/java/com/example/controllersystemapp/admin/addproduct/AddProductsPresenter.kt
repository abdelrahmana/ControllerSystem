package com.example.controllersystemapp.admin.addproduct

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response


object AddProductsPresenter {

    fun getAddEditProduct(webService: WebService, addProductRequest: AddProductRequest?, activity: Activity, model: ViewModelHandleChangeFragmentclass,
    edit:Boolean=false,productId :Int?=null)
    {

        var builder = MultipartBody.Builder()

        var bitmapUpdatedImage: Bitmap? = null
        if(addProductRequest?.images!!.isEmpty()) {
            setAddEditProduct(
                builder,
                addProductRequest,
                webService,
                model,
                edit,
                productId,
                activity
            )
            return
        }

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
                            setAddEditProduct(builder,addProductRequest,webService,model,edit,productId,activity)



                        }






                    }
                })
        }
        setAddEditProduct(builder,addProductRequest,webService,model,edit,productId,activity)




    }

    private fun setAddEditProduct(
        builder: MultipartBody.Builder,
        addProductRequest: AddProductRequest,
        webService: WebService,
        model: ViewModelHandleChangeFragmentclass,
        edit: Boolean,
        productId: Int?,
        activity: Activity
    ) {
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


        if(edit)
            builder.addFormDataPart("id",
                productId.toString())
        val requestBody = builder.build()
        var addEditProduct = webService.addProduct(requestBody)
        if (edit)
            addEditProduct = webService.editProduct(requestBody)
        // Log.d("testApi" , "getData")
        addEditProduct.subscribeOn(Schedulers.io())
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