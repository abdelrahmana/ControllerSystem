package com.example.controllersystemapp.callcenter.delegate.itemdetails

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.products.adapters.SlidingItemImageAdapter
import com.example.controllersystemapp.accountant.products.models.Image
import com.example.controllersystemapp.callcenter.delegate.DelegatePresenter
import com.example.controllersystemapp.callcenter.delegate.model.DataBeans
import com.example.controllersystemapp.callcenter.delegate.model.ItemDetailsResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_item_details.*
import retrofit2.Response


class ItemDetailsFragment : Fragment(), OnMapReadyCallback {

    lateinit var slidingItemImageAdapter: SlidingItemImageAdapter
    var slideImage = ArrayList<Image>()

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_item_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        orderMap.onCreate(savedInstanceState);
        //orderMap.getMapAsync(this)
        closePageIcon?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

      //  observeData()
    }

 /*   private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ItemDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    getItemDetails(datamodel.data?: DataBeans())
                }

//                if (datamodel is SuccessModel) {
//                    Log.d("testApi", "isForyou")
//                    successRemove(datamodel)
//                }
                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.errorMessage.observe(activity!! , Observer { error ->
            if (error != null)
            {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })



    }*/

    private fun getItemDetails(data: DataBeans) {
            prodName?.text = data.product?.name?:""
            prodQuantity?.text = data.product?.total_quantity?:""
            prodCategoryName?.text = data.product?.category?.name?:""
         //   prodStoreName?.text = data.ware_houses?.get(0)?.name?:""
            prodStoreQuantity?.text = data.quantity.toString()

            setSliderImages(data.product?.image)

            prodDetailsDescription?.text = data.product?.description?:""
            totalPricePrice?.text = data.order?.total_price?:""
            totalPriceCurrancy?.text = data.order?.currency?:""
        mapAddress?.text= data.order?.address?:""
        mapName?.text = data.order?.name?:""
        mapPhone?.text = data.order?.phone?:""
        mapEmail?.text = data.order?.email?:""

    }

    private fun setSliderImages(detailsData: String?){
        val images=Image(image=detailsData)

        if (detailsData?.isNullOrEmpty() == false)
        {
            slideImage.clear()
            slideImage.add(images)

            slidingItemImageAdapter =
                SlidingItemImageAdapter(
                    context!!,
                    slideImage
                )
            sliderImage?.adapter = slidingItemImageAdapter
            indicator.setViewPager(sliderImage)

        }
        else{
            //empty
            slideImage.clear()
            slideImage.add(
               images
            )

            slidingItemImageAdapter =
                SlidingItemImageAdapter(
                    context!!,
                    slideImage
                )
            sliderImage?.adapter = slidingItemImageAdapter
            indicator.setViewPager(sliderImage)
        }


    }

    override fun onResume() {
       // orderMap.onResume()
        super.onResume()

        getData()

    }

    override fun onDestroyView() {
        disposableObserver?.dispose()
        model.notifyItemSelected.removeObservers(activity!!)
        super.onDestroyView()
    }

    var disposableObserver : DisposableObserver<Response<ItemDetailsResponse>>?=null
    private fun ItemListDetailsObserver(): DisposableObserver<Response<ItemDetailsResponse>> {

        disposableObserver= object : DisposableObserver<Response<ItemDetailsResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<ItemDetailsResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    getItemDetails(response.body()?.data?: DataBeans())
                }
                else
                {
                    progressDialog?.dismiss()
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
        return disposableObserver!!
    }
    private fun getData() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val hashMap = HashMap<String,Any>()
            hashMap.put("item_id",arguments?.getInt(NameUtils.itemId,0)?:0)
            DelegatePresenter.getItemDetails(webService!!, ItemListDetailsObserver(),hashMap)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    override fun onMapReady(p0: GoogleMap?) {
0
    }




    override fun onPause() {
        super.onPause()
       // orderMap.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    //    orderMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
      //  orderMap.onLowMemory()
    }
}