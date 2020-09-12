package com.example.controllersystemapp.admin.addproduct

import android.R.attr
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.addproduct.ScanCodeActivity.Companion.REQUEST_BARCODE
import com.example.controllersystemapp.admin.addproduct.ScanCodeActivity.Companion.RES_CODE_B
import com.example.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.example.controllersystemapp.admin.storeclassification.StoreClassificationFragment
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.UtilKotlin.performImgPicAction
import com.example.util.UtilKotlin.permissionForImageAndFile
import com.example.util.UtilKotlin.showSnackErrorInto
import com.example.util.UtilKotlin.submitPermssion
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.io.IOException


class AddProductFragment : Fragment() {


    var whichOpen : Int = 0
    var productImagesList: ArrayList<String>? = ArrayList()
    var quantityList: ArrayList<Int>? = ArrayList()
    var warehouse_id: ArrayList<Int>? = ArrayList()

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

        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("viewCreated", "true")

        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        materialProduct?.setOnClickListener {
         UtilKotlin.changeFragmentBack(activity!! , FragmentProductclassification() , "productClassification"  ,
             null , R.id.frameLayout_direction)
        }

        materialSave?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! , StoreClassificationFragment() , "storeClassification"  ,
                null , R.id.frameLayout_direction)
        }

        openCamLayout?.setOnClickListener{

            if (UtilKotlin.checkPermssionGrantedForImageAndFile(activity!!, permissionForImageAndFile,this)){
                // if the result ok go submit else on
                whichOpen = 0
                performImgPicAction(0 , this , activity!!)


            }

        }

        openScanCamera?.setOnClickListener {


           //IntentIntegrator(activity!!).initiateScan() // `this` is the current Activity

            val intent = Intent(activity , ScanCodeActivity::class.java)
            startActivity(intent)


        }

        openGalleryLayout?.setOnClickListener{

            if (UtilKotlin.checkPermssionGrantedForImageAndFile(activity!!, permissionForImageAndFile,this)){
                // if the result ok go submit else on
                whichOpen = 1
                performImgPicAction(1 , this , activity!!)


            }

        }

        addProductButton?.setOnClickListener {

            if (UtilKotlin.isNetworkAvailable(context!!)) {
                progressDialog?.show()
                makeRequest()
            } else {
                progressDialog?.dismiss()
                showSnackErrorInto(activity, getString(R.string.no_connect))
            }
        }



    }

    override fun onResume() {
        super.onResume()
        observeAdd()

        Log.d("viewCreated", "resume")
        Log.d("code", "$BARCODE")

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

                if (datamodel is Int) {
                    Log.d("observeData", "dd $datamodel")

                }

                model.responseCodeDataSetter(null) // start details with this data please
            }
            else{
                Log.d("testApi", "observeNull")

            }

        })

        model.errorMessage.observe(activity!! , Observer { error ->

            if (error != null)
            {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                Log.d("error", "error $errorFinal")
                showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })


    }

    private fun getAddProductData(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            Log.d("success", "error ${successModel?.msg.get(0)}")
            UtilKotlin.showSnackMessage(activity , successModel?.msg.get(0))
        }



    }

    private fun makeRequest() {
        quantityList?.clear()
        warehouse_id?.clear()

        Log.d("testSize" , "${productImagesList?.size}")
        //Log.d("testSize" , "${productImagesList?.get(0)}")

        quantityList?.add(50)
        warehouse_id?.add(1)

        Log.d("codeData" , "$BARCODE")

        val addProductRequest = AddProductRequest(productNameEditText?.text?.toString() ,
            describeProductEditText?.text?.toString() , priceEditText?.text?.toString() , BARCODE ,
            1 , quantityList , warehouse_id , productImagesList)

       // AddProductsPresenter.getAddProduct(webService!! , addProductRequest , activity!! , model)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == submitPermssion) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //  uploadNewImageFirst()
               // performImgPicAction(whichOpen , this , activity!!)

            } else {
                showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        }
        else if (requestCode == permissionForImageAndFile) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                performImgPicAction(whichOpen , this , activity!!)

            } else {
                showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        }
    }


    var bitmapUpdatedImage : Bitmap? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentURI = data.data
                try {
                    bitmapUpdatedImage = BitmapFactory.decodeStream(activity!!.contentResolver.openInputStream(contentURI!!))
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
        }

    }
//E/Parcel: Class not found when unmarshalling: com.google.android.apps.photos.allphotos.data.AllPhotosCollection
//    java.lang.ClassNotFoundException: com.google.android.apps.photos.allphotos.data.AllPhotosCollection
    private fun addImageToList(bitmapUpdatedImage: Bitmap) {
       // productImagesList?.clear()

        val file = UtilKotlin.getCreatedFileFromBitmap("image",bitmapUpdatedImage!!,"jpg",context!!)
        productImagesList?.add(file.absolutePath)
        Log.d("Size" , "${productImagesList?.size}")
        Log.d("Size" , "${productImagesList?.get(0)}")
        // lets notify adaptor now
        //model?.setNotifyItemSelected(imageModel) // update other lists

    }


    companion object {
        val GALLERY = 1
        val CAMERA = 0
        var BARCODE = ""

    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}