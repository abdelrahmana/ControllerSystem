package com.smartangle.controllersystemapp.admin.addproduct

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.smartangle.controllersystemapp.admin.storeclassification.StoreClassificationFragment
import com.smartangle.controllersystemapp.admin.storesproducts.models.StoresListResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils.WHICH_ADD_PRD_STORE
import com.smartangle.util.UtilKotlin
import com.smartangle.util.UtilKotlin.performImgPicAction
import com.smartangle.util.UtilKotlin.permissionForImageAndFile
import com.smartangle.util.UtilKotlin.permissionScan
import com.smartangle.util.UtilKotlin.showSnackErrorInto
import com.smartangle.util.UtilKotlin.submitPermssion
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.android.synthetic.main.fragment_add_product.barCodeTxt
import java.io.IOException


class AddProductFragment : Fragment() {


    var whichOpen: Int = 0

    lateinit var imagesListAdaptor: ImagesListAdaptor
    var productImagesList: ArrayList<String>? = ArrayList()

    var quantityList: ArrayList<Int>? = ArrayList()
    var warehouse_id: ArrayList<Int>? = ArrayList()

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog

    var categoryID = 0
    var parentCategoryID = 0

    var barCode: String? = null
    var productCategoryName: String? = null
    var storeName = ""


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

        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("viewCreated", "true")

        observeImagesData()

        setAdaptorItem()

        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        materialProduct?.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(WHICH_ADD_PRD_STORE, R.id.frameLayout_direction)
            UtilKotlin.changeFragmentBack(
                activity!!, FragmentProductclassification(), "productClassification",
                bundle, R.id.frameLayout_direction
            )
        }

        materialSave?.setOnClickListener {

            if (categoryID == 0) {
                showSnackErrorInto(activity!!, getString(R.string.selectCategoryFirst))
            }
            else{
                val bundle = Bundle()
                bundle.putInt(PARENT_CATEGORY_ID_STORE , parentCategoryID?:-1)
                UtilKotlin.changeFragmentBack(
                    activity!!, StoreClassificationFragment(), "storeClassification",
                    bundle , R.id.frameLayout_direction
                )
            }


        }

        openCamLayout?.setOnClickListener {
            whichOpen = 0

            if (UtilKotlin.checkPermssionGrantedForImageAndFile(
                    activity!!,
                    permissionForImageAndFile,
                    this
                )
            ) {
                // if the result ok go submit else on
                performImgPicAction(0, this, activity!!)


            }

        }


        openGalleryLayout?.setOnClickListener {
            whichOpen = 1

            if (UtilKotlin.checkPermssionGrantedForImageAndFile(
                    activity!!,
                    permissionForImageAndFile,
                    this
                )
            ) {
                // if the result ok go submit else on
                performImgPicAction(1, this, activity!!)


            }

        }

        openScanCamera?.setOnClickListener {

            //IntentIntegrator(activity!!).initiateScan() // `this` is the current Activity
            if (UtilKotlin.checkPermssionGrantedForImageAndFile(activity!!, permissionScan, this)) {
                // if the result ok go submit else on
                val intent = Intent(activity, ScanCodeActivity::class.java)
                startActivityForResult(intent, ScanCodeActivity.scanCode)

            }


        }



        addProductButton?.setOnClickListener {

            checkValidation()

        }

        observeAdd()

    }

    private fun checkValidation() {

        var errorMessage = ""

//        if (productImagesList?.isNullOrEmpty() == true) {
//            errorMessage += getString(R.string.select_product_image)
//            errorMessage += "\n"
//        }

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
        if (priceEditTextSale.text.isNullOrBlank()) {
            errorMessage += getString(R.string.price_sale_required)
            errorMessage += "\n"
        }

        if (barCode == null || barCode == "") {
            errorMessage += getString(R.string.barecode_required)
            errorMessage += "\n"
        }

        if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
        } else {


            requestAddProduct()

        }


    }

    private fun requestAddProduct() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            makeRequest()
        } else {
            progressDialog?.dismiss()
            showSnackErrorInto(activity, getString(R.string.no_connect))
        }


    }

    private fun observeImagesData() {

        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { datamodel ->
            // calls
            if (datamodel != null) { // lets use this as share please
                if (datamodel is String) // if it is object of this model
                {
                    //imageModel = datamodel
                    setAdaptorItem()

                    //getData(datamodel) // move data to here please
                }
                // when result is coming
                // here we should set every thing related to this details activity
                model?.setNotifyItemSelected(null)

            }
        })


    }

    private fun setAdaptorItem() {

        imagesListAdaptor = ImagesListAdaptor(
            UtilKotlin.declarViewModel(activity!!)!!,
            productImagesList ?: ArrayList()
        )

        val linearLayoutManager: LinearLayoutManager? =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        imagesListRecycler.layoutManager = linearLayoutManager
        imagesListRecycler.setHasFixedSize(true)
        imagesListRecycler.adapter = imagesListAdaptor


    }

    override fun onResume() {
        super.onResume()

        Log.d("viewCreated", "resume")
        //Log.d("code", "$BARCODE")
        //barCodeTxt?.text = BARCODE?:""
        barCodeTxt?.text = barCode
        addProdClassifyTxt?.text = productCategoryName
        storeClassificationTxt?.text = storeName

    }

    private fun observeAdd() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
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
                    parentCategoryID = datamodel.parentId ?: -1
                    productCategoryName = "${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}"
                    addProdClassifyTxt?.text = productCategoryName
                }

                //StoreIdQuantity
                if (datamodel is StoresListResponse) {//when choose category return categoryID
//                    Log.d("observeData", "quantityId ${datamodel.quantityId.size}")
//                    Log.d("observeData", "storeId ${datamodel.storeId.size}")
                    quantityList?.clear()
                    warehouse_id?.clear()


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
                    storeName = ""
                    if (datamodel.data?.size ?: 0 > 0) {
                        for (i in 0 until datamodel.data?.size!!) {
                            quantityList?.add(datamodel.data[i].quantity ?: 1)
                            warehouse_id?.add(datamodel.data[i].id ?: 0)
                            storeName += datamodel?.data?.get(i).name ?: ""
                            if (i != datamodel?.data?.size - 1)
                                storeName += " - "

                        }
                    }
//                    Log.d("quantityFinalResult", " size${quantityList?.size}")
//                    Log.d("warehouseFinalResult", " size ${warehouse_id?.size}")
//
//                    for (i in quantityList?.indices!!) {
//                        Log.d("quantityFinalResult", " Data $i ${quantityList?.get(i)}")
//                    }
//                    for (i in warehouse_id?.indices!!) {
//                        Log.d("warehouseFinalResult", " Data $i ${warehouse_id?.get(i)}")
//                    }
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
                progressDialog?.hide()
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
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                Log.d("error", "error $errorFinal")
                showSnackErrorInto(activity!!, errorFinal)
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

    private fun makeRequest() {

        Log.d("barcod", "bar $barCode")
        val addProductRequest = AddProductRequest(
            productNameEditText?.text?.toString(),
            describeProductEditText?.text?.toString(), priceEditText?.text?.toString(),priceEditTextSale?.text.toString(), barCode,
            categoryID, quantityList, warehouse_id, productImagesList
        )
     //   AddProductsPresenter.getAddProduct(webService!!, addProductRequest, activity!!, model)
        AddProductsPresenter.getAddEditProduct(
            webService!!,
            addProductRequest,
            activity!!,
            model,
            false,null
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == submitPermssion) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //  uploadNewImageFirst()
                // performImgPicAction(whichOpen , this , activity!!)
                //performImgPicAction(whichOpen , this , activity!!)

            } else {
                showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        } else if (requestCode == permissionForImageAndFile) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                performImgPicAction(whichOpen, this, activity!!)

            } else {
                showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        } else if (requestCode == permissionScan) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                val intent = Intent(activity, ScanCodeActivity::class.java)
                startActivityForResult(intent, ScanCodeActivity.scanCode)

            } else {
                showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        }
    }


    var bitmapUpdatedImage: Bitmap? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY && resultCode == Activity.RESULT_OK) {
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
        } else if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) { // async with dialog show
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
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }

    //E/Parcel: Class not found when unmarshalling: com.google.android.apps.photos.allphotos.data.AllPhotosCollection
//    java.lang.ClassNotFoundException: com.google.android.apps.photos.allphotos.data.AllPhotosCollection
    private fun addImageToList(bitmapUpdatedImage: Bitmap) {
        // productImagesList?.clear()

        val file =
            UtilKotlin.getCreatedFileFromBitmap("image", bitmapUpdatedImage!!, "jpg", context!!)
        productImagesList?.add(file.absolutePath)
        Log.d("Size", "${productImagesList?.size}")
        Log.d("Size", "${productImagesList?.get(0)}")
        // lets notify adaptor now
        //model?.setNotifyItemSelected(imageModel) // update other lists
        model?.setNotifyItemSelected("select") // update other lists
    }


    companion object {
        val GALLERY = 1
        val CAMERA = 0
        val PARENT_CATEGORY_ID_STORE = "parentCategoryIdStore"
        //var BARCODE = ""

    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)
            it?.notifyItemSelected?.removeObservers(activity!!)


        }
        super.onDestroyView()
    }
}