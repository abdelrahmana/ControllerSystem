package com.example.controllersystemapp.admin.productclassification.productsubclassification

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.CategoriesPresenter
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.example.controllersystemapp.admin.productclassification.lastsubcategory.FragmentLastSubProductclassification
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils.productId
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.ProductClassificationAdaptor
import kotlinx.android.synthetic.main.fragment_product_classification.*

class FragmentSubProductclassification : Fragment() {


    lateinit var model : ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    var subProductAdaptor : SubProductClassificationAdaptor?=null
    var subCategoryList = ArrayList<Data>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model =UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_product_classification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subCategroyHeader?.text = arguments?.getString(FragmentProductclassification.PARENT_NAME)?:""
        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
           //setRecycleViewData() // set recycleView
        setViewModelListener() // when select item
    }

    override fun onResume() {
        super.onResume()

        getSubData()
    }

    private fun getSubData() {


        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenter.getCategoriesList(webService!!
                , arguments?.getInt(FragmentProductclassification.PARENT_ID)?:-1
                , activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    override fun onDestroyView() {
        unSubscribeListners()
        super.onDestroyView()
    }
    fun unSubscribeListners() {
        model.let {
            // not from all listner
            it?.responseDataCode?.removeObservers(activity!!) // remove observer from here only
            it?.notifyItemSelected?.removeObservers(activity!!)
            it?.errorMessage?.removeObservers(activity!!)

        }

    }
    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {

        if (categoriesListResponse.data?.isNullOrEmpty() == false) {

            subCategoryList.clear()
            subCategoryList.addAll(categoriesListResponse?.data)
            subProductAdaptor = SubProductClassificationAdaptor(model, subCategoryList)
            UtilKotlin.setRecycleView(productList,subProductAdaptor!!,
                RecyclerView.VERTICAL,context!!, null, true)
        } else {
            //empty
        }





//        val arrayList = ArrayList<Any>()
//        arrayList.add("العاب")
//        arrayList.add("منزل")
//        arrayList.add("اجهزة منزلية")
//        subProductAdaptor = SubProductClassificationAdaptor(model,arrayList)
//        UtilKotlin.setRecycleView(productList,subProductAdaptor!!,
//            RecyclerView.VERTICAL,context!!, null, true)
    }

    fun setViewModelListener() {
        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Data) {
                    // if (modelSelected.isItCurrent) {
                    // initSlider(modelSelected.pictures)
                    // }
                //    model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
                    val bundle = Bundle()
                    //     bundle.putInt(EXITENCEIDPACKAGE,availableServiceList.get(position).id?:-1)
                    Log.d("subParetnId" , "observeParent ${modelSelected.id}")
                    Log.d("subParetnId" , "observeParentId ${arguments?.getInt(FragmentProductclassification.PARENT_ID)}")

                    bundle.putInt(FragmentProductclassification.SUB_PARENT_ID, modelSelected.id?:-1)
                    bundle.putString(FragmentProductclassification.PARENT_NAME, modelSelected.name?:"")
                    bundle.putInt(FragmentProductclassification.PARENT_ID, arguments?.getInt(FragmentProductclassification.PARENT_ID)?:-1)
                    UtilKotlin.changeFragmentWithBack(activity!! , R.id.frameLayout_direction , FragmentLastSubProductclassification() , bundle)
                }
                /* else if (modelSelected is ImageModelData) // if it is object of this model
                  {
                      /*  val pictures = ArrayList<Picture>()
                        datamodel.image?.forEach{
                            pictures.add(Picture(it))

                        }*/

                      //  initSlider(pictures) // add these services to image
                      //getData(datamodel) // move data to here please
                  }
  */
                model?.setNotifyItemSelected(null)
            }
        })

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is CategoriesListResponse) {
                    Log.d("testApi", "isForyou")
                    setRecycleViewData(datamodel)

                }
                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.errorMessage.observe(activity!!, Observer { error ->

            if (error != null) {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })



    }
}