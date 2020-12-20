package com.smartangle.controllersystemapp.admin.storesproducts.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.addproduct.*
import com.smartangle.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.smartangle.controllersystemapp.admin.storeclassification.StoreClassificationFragment
import com.smartangle.controllersystemapp.admin.storesproducts.ProductsPresenter
import com.smartangle.controllersystemapp.admin.storesproducts.adapters.ImagesEditListAdaptor
import com.smartangle.controllersystemapp.admin.storesproducts.models.Image
import com.smartangle.controllersystemapp.admin.storesproducts.models.StoresListResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_edit_product.*
import retrofit2.Response
import java.io.IOException

 // send details data when click on edit button
// delete image api and observer
// test real data
class EditProductFragment : Fragment() {
    var quantityList: ArrayList<Int>? = ArrayList()
    var warehouse_id: ArrayList<Int>? = ArrayList()
    var categoryID = 0
    var barCode: String? = null
    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog
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
        return inflater.inflate(R.layout.fragment_edit_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeImagesData()
        observeUpdate()

        setProductDetailsCurrent()

        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        materialProduct?.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(NameUtils.WHICH_ADD_PRD_STORE, R.id.frameLayout_direction)
            UtilKotlin.changeFragmentBack(
                activity!!, FragmentProductclassification(), "productClassification",
                bundle, R.id.frameLayout_direction
            )
        }

        materialSave?.setOnClickListener {
            UtilKotlin.changeFragmentBack(
                activity!!, StoreClassificationFragment(), "storeClassification",
                null, R.id.frameLayout_direction
            )
        }

        openCamLayout?.setOnClickListener {
            whichOpen = 0

            if (UtilKotlin.checkPermssionGrantedForImageAndFile(
                    activity!!,
                    UtilKotlin.permissionForImageAndFile,
                    this
                )
            ) {
                // if the result ok go submit else on
                UtilKotlin.performImgPicAction(0, this, activity!!)


            }

        }


        openGalleryLayout?.setOnClickListener {
            whichOpen = 1

            if (UtilKotlin.checkPermssionGrantedForImageAndFile(
                    activity!!,
                    UtilKotlin.permissionForImageAndFile,
                    this
                )
            ) {
                // if the result ok go submit else on
                UtilKotlin.performImgPicAction(1, this, activity!!)


            }

        }

        openScanCamera?.setOnClickListener {

            //IntentIntegrator(activity!!).initiateScan() // `this` is the current Activity
            if (UtilKotlin.checkPermssionGrantedForImageAndFile(activity!!,
                    UtilKotlin.permissionScan, this)) {
                // if the result ok go submit else on
                val intent = Intent(activity, ScanCodeActivity::class.java)
                startActivityForResult(intent, ScanCodeActivity.scanCode)

            }


        }



        addProductButton?.setOnClickListener {

            checkValidation()

        }
    }
     var imagesListAdaptor : ImagesEditListAdaptor?=null
     private fun setAdaptorItem() {

         imagesListAdaptor = ImagesEditListAdaptor(
             UtilKotlin.declarViewModel(activity!!)!!,
             productImagesList ?: ArrayList()
         )

         val linearLayoutManager: LinearLayoutManager? =
             LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
         imagesListRecycler.layoutManager = linearLayoutManager
         imagesListRecycler.setHasFixedSize(true)
         imagesListRecycler.adapter = imagesListAdaptor


     }
     private fun setProductDetailsCurrent() {
         val productString = arguments?.getString(NameUtils.PRODUCT_DETAILS)?:""
       val productDetails = UtilKotlin.getProductDetails(productString)
         productImagesList?.clear()
        productImagesList?.addAll(productDetails?.data?.images?:ArrayList())
         setAdaptorItem() // set image adapter

         productNameEditText?.setText(productDetails?.data?.name?:"")
         describeProductEditText?.setText(productDetails?.data?.description?:"")
         describeProductEditText?.setText(productDetails?.data?.description?:"")
         addProdClassifyTxt?.setText(productDetails?.data?.category?.name?:"")

         var storeName = ""
         if (productDetails?.data?.ware_houses?.size?: 0 > 0) {
             for (i in 0 until productDetails?.data?.ware_houses?.size!!) {
                 quantityList?.add((productDetails.data?.ware_houses[i].pivot?.quantity ?:"1").toInt())
                 warehouse_id?.add((productDetails.data?.ware_houses[i].id ?: 0))
                 storeName += productDetails.data?.ware_houses[i].name?:""
                 if (i != productDetails?.data?.ware_houses?.size - 1)
                     storeName += " - "

             }
         }
         storeClassificationTxt?.setText(storeName) // store classification name

         priceEditText?.setText(productDetails?.data?.price?:"")
         barCode = productDetails?.data?.barcode?:""
         categoryID = (productDetails?.data?.category_id?:"0").toInt()
         barCodeTxt.text = productDetails?.data?.barcode?:""
         productId = productDetails?.data?.id?:0
     }

     var productImagesList: ArrayList<Image>? = ArrayList()
     var productId : Int = 0
    private fun checkValidation() {

        var errorMessage = ""

        if (productImagesList?.isNullOrEmpty() == true) {
            errorMessage += getString(R.string.select_product_image)
            errorMessage += "\n"
        }

        if (productNameEditText.text.isNullOrBlank()) {
            errorMessage += getString(R.string.product_name_required)
            errorMessage += "\n"
        }

        if (describeProductEditText.text.isNullOrBlank()) {
            errorMessage += getString(R.string.product_desc_required)
            errorMessage += "\n"
        }

        if (categoryID == 0) {
            errorMessage += getString(R.string.product_classify_required)
            errorMessage += "\n"
        }

        if (warehouse_id?.isNullOrEmpty() == true && quantityList?.isNullOrEmpty() == true) {
            errorMessage += getString(R.string.select_store_classify)
            errorMessage += "\n"
        }

        if (priceEditText.text.isNullOrBlank()) {
            errorMessage += getString(R.string.price_required)
            errorMessage += "\n"
        }

        if (barCode == null || barCode == "") {
            errorMessage += getString(R.string.barecode_required)
            errorMessage += "\n"
        }

        if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
        } else {


            editRequest()

        }
    }
    private fun observeImagesData() {

        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { datamodel ->
            // calls
            if (datamodel != null) { // lets use this as share please
                if (datamodel is Image) // if it is object of this model
                {
                    if (datamodel.id==0) // local delete only
                    {
                        productImagesList?.removeAt(datamodel.position) // remove
                        imagesListAdaptor?.notifyDataSetChanged()
                    }

                    else {
                        progressDialog?.show()
                        ProductsPresenter.deleteImage(
                            webService!!,
                            ItemListObserver(datamodel.position), datamodel.id ?: 0
                        )
                    }
                    //else // call delete api
                    //imageModel = datamodel
                   // setAdaptorItem()

                    //getData(datamodel) // move data to here please
                }
                if (datamodel is String) {
                    imagesListAdaptor?.notifyDataSetChanged()

                }
                // when result is coming
                // here we should set every thing related to this details activity
                model?.setNotifyItemSelected(null)

            }
        })


    }
     var disposableObserverDelete : DisposableObserver<Response<SuccessModel>>?=null
     private fun ItemListObserver(position : Int): DisposableObserver<Response<SuccessModel>> {

         disposableObserverDelete= object : DisposableObserver<Response<SuccessModel>>() {
             override fun onComplete() {
                 progressDialog?.dismiss()
                 dispose()
             }

             override fun onError(e: Throwable) {
                 UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                 progressDialog?.dismiss()
                 dispose()
             }

             override fun onNext(response: Response<SuccessModel>) {
                 if (response!!.isSuccessful) {
                     progressDialog?.dismiss()

                     UtilKotlin.showSnackMessage(activity!!, response.body()?.msg?.get(0)?:"")
                     productImagesList?.removeAt(position) // remove
                     imagesListAdaptor?.notifyDataSetChanged()
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
         return disposableObserverDelete!!
     }

    var whichOpen = 0
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == UtilKotlin.submitPermssion) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //  uploadNewImageFirst()
                // performImgPicAction(whichOpen , this , activity!!)
                //performImgPicAction(whichOpen , this , activity!!)

            } else {
                UtilKotlin.showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        } else if (requestCode == UtilKotlin.permissionForImageAndFile) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                UtilKotlin.performImgPicAction(whichOpen, this, activity!!)

            } else {
                UtilKotlin.showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        } else if (requestCode == UtilKotlin.permissionScan) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                val intent = Intent(activity, ScanCodeActivity::class.java)
                startActivityForResult(intent, ScanCodeActivity.scanCode)

            } else {
                UtilKotlin.showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        }
    }

        private fun observeUpdate() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.dismiss()
                Log.d("testApi", "responseNotNull")

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    getAddProductData(datamodel)
                }

//                if (datamodel is Int) {//when choose category return categoryID
//                    Log.d("observeData", "dd $datamodel")
//                    categoryID = datamodel
//                }

                if (datamodel is ViewModelHandleChangeFragmentclass.ProductClassification) {//when choose category return categoryID
                    Log.d("observeData", "dd $datamodel")
                    categoryID = datamodel.id ?: -1
                    Log.d(
                        "finalText",
                        "${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}"
                    )
                    addProdClassifyTxt?.text = "${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}"
                }

                //StoreIdQuantity
                if (datamodel is StoresListResponse) {//when choose category return categoryID
//                    Log.d("observeData", "quantityId ${datamodel.quantityId.size}")
//                    Log.d("observeData", "storeId ${datamodel.storeId.size}")
                    quantityList?.clear()
                    warehouse_id?.clear()
                    var storeName = ""


//                    for (i in 0 until datamodel.quantityId.size)
//                    {
//                        quantityList?.add(datamodel.quantityId[i])
//                    }
//
//                    for (i in 0 until datamodel.storeId.size)
//                    {
//
//                        warehouse_id?.add(datamodel.storeId[i])
//                    }
                    if (datamodel.data?.size ?: 0 > 0) {
                        for (i in 0 until datamodel.data?.size!!) {
                            quantityList?.add(datamodel.data[i].quantity ?: 1)
                            warehouse_id?.add(datamodel.data[i].id ?: 0)
                            storeName += datamodel?.data?.get(i).name ?: ""
                            if (i != datamodel?.data?.size - 1)
                                storeName += " - "

                        }
                    }
                    Log.d("quantityFinalResult", " size${quantityList?.size}")
                    Log.d("warehouseFinalResult", " size ${warehouse_id?.size}")

                    for (i in quantityList?.indices!!) {
                        Log.d("quantityFinalResult", " Data $i ${quantityList?.get(i)}")
                    }
                    for (i in warehouse_id?.indices!!) {
                        Log.d("warehouseFinalResult", " Data $i ${warehouse_id?.get(i)}")
                    }
                    storeClassificationTxt?.text = storeName


                }

                model.responseCodeDataSetter(null) // start details with this data please
            } else {
                Log.d("testApi", "observeNull")

            }

        })

        model.stringDataVar?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.dismiss()
                Log.d("testApi", "responseNotNull")
                Log.d("resultData", datamodel) // Prints scan results
                barCode = datamodel

                model.setStringVar(null) // start details with this data please
            } else {
                Log.d("testApi", "observeNull")

            }

        })

        model.errorMessage.observe(activity!!, Observer { error ->

            if (error != null) {
                progressDialog?.dismiss()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                Log.d("error", "error $errorFinal")
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })


    }

    private fun getAddProductData(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false) {
            UtilKotlin.showSnackMessage(activity, successModel?.msg[0])
            Handler().postDelayed(Runnable {
                activity?.let {
                    it.supportFragmentManager.popBackStack()
                }
            }, 1000)
        }

    }

    private fun editRequest() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {

            progressDialog?.show()
            var productList = ArrayList<String>()
            productImagesList?.forEach {
                if (it?.id == 0)
                    productList.add(it?.image ?: "")
            }
            val addProductRequest = AddProductRequest(
                productNameEditText?.text?.toString(),
                describeProductEditText?.text?.toString(), priceEditText?.text?.toString(), barCode,
                categoryID, quantityList, warehouse_id, productList
            )

            AddProductsPresenter.getAddEditProduct(
                webService!!,
                addProductRequest,
                activity!!,
                model,
                true,productId
            )
        }else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
    }
    var bitmapUpdatedImage: Bitmap? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddProductFragment.GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentURI = data.data
                try {
                    bitmapUpdatedImage = BitmapFactory.decodeStream(
                        activity!!.contentResolver.openInputStream(contentURI!!)
                    )
                    addImageToList(bitmapUpdatedImage!!)
                    //  val file =getCreatedFileFromBitmap("image",bitmapUpdatedImage!!,"jpg",context!!)
                    //   imageModel.image?.add(file.absolutePath)
                    //.setImageBitmap(bitmapUpdatedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == AddProductFragment.CAMERA && resultCode == Activity.RESULT_OK) { // async with dialog show
            bitmapUpdatedImage = data!!.extras!!["data"] as Bitmap?
            addImageToList(bitmapUpdatedImage!!)
            // userImage.setImageBitmap(bitmapUpdatedImage)
        } else if (requestCode == ScanCodeActivity.scanCode) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra(ScanCodeActivity.SCANERESULT)
                Log.d("resultData", result) // Prints scan results
                barCode = result
                barCodeTxt?.text = result
            }
          /*  if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }*/
        }

    }

    //E/Parcel: Class not found when unmarshalling: com.google.android.apps.photos.allphotos.data.AllPhotosCollection
//    java.lang.ClassNotFoundException: com.google.android.apps.photos.allphotos.data.AllPhotosCollection
    private fun addImageToList(bitmapUpdatedImage: Bitmap) {
        // productImagesList?.clear()

        val file =
            UtilKotlin.getCreatedFileFromBitmap("image", bitmapUpdatedImage!!, "jpg", context!!)
        productImagesList?.add(Image(image= file.absolutePath)) // a
        // lets notify adaptor now
        //model?.setNotifyItemSelected(imageModel) // update other lists
        model?.setNotifyItemSelected("select") // update other lists
    }
    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.stringNameData?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)
            it?.notifyItemSelected?.removeObservers(activity!!)


        }
        disposableObserverDelete?.dispose()
        super.onDestroyView()
    }
}