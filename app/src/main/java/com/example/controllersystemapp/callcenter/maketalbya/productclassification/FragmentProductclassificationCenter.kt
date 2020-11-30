package com.example.controllersystemapp.admin.productclassification

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
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.productclassification.productsubclassification.FragmentSubProductclassificationCenter
import com.example.controllersystemapp.admin.productclassification.productsubclassification.SubSubProductListFragment
import com.example.controllersystemapp.callcenter.maketalbya.CategoriesPresenterCallCenter
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils.WHICH_ADD_PRD_STORE
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.ProductClassificationAdaptorCenter
import kotlinx.android.synthetic.main.fragment_product_classification.*

class FragmentProductclassificationCenter : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    var productAdaptor: ProductClassificationAdaptorCenter? = null
    var categoriesParentList = ArrayList<Data>()


    var addProductOrStore : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_classification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // handleViewVisibilty()

//        addProductButton?.setOnClickListener {
//
//            activity?.supportFragmentManager?.popBackStack()
//
//        }

        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
        //setRecycleViewData()
        setViewModelListener()
    }

//    private fun handleViewVisibilty() {
//
//        val whichProductStore = arguments?.getString(WHICH_ADD_PRD_STORE)?:""
//        when(whichProductStore)
//        {
//            ADD_PRODUCT -> {
//
//                addProductOrStore = ADD_PRODUCT
//                //addProductButton?.visibility = View.GONE
//
//
//            }
//
//            ADD_STORE -> {
//
//                addProductOrStore = ADD_STORE
//                //addProductButton?.visibility = View.VISIBLE
//
//
//            }
//
//
//        }
//
//
//    }

    override fun onResume() {
        super.onResume()

        getData()


    }

    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenterCallCenter.getCategoriesList(webService!!, null, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {


        if (categoriesListResponse.data?.isNullOrEmpty() == false) {

            categoriesParentList.clear()
            categoriesParentList.addAll(categoriesListResponse?.data)
            productAdaptor = ProductClassificationAdaptorCenter(model, categoriesParentList)
            UtilKotlin.setRecycleView(
                productList, productAdaptor!!,
                RecyclerView.VERTICAL, context!!, null, true
            )
        } else {
            //empty
        }


//        val arrayList = ArrayList<Any>()
//        arrayList.add("العاب")
//        arrayList.add("منزل")
//        arrayList.add("اجهزة منزلية")
//        productAdaptor = ProductClassificationAdaptor(model,arrayList)
//        UtilKotlin.setRecycleView(productList,productAdaptor!!,
//            RecyclerView.VERTICAL,context!!, null, true)
    }

    override fun onDestroyView() {
        model?.notifyItemSelected?.removeObservers(activity!!)
        model?.responseDataCode?.removeObservers(activity!!)
        model?.errorMessage?.removeObservers(activity!!)

        super.onDestroyView()
    }

    fun setViewModelListener() {
        Log.d("paretnId" , "jj")

        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                Log.d("paretnId" , "observeParent")

                if (modelSelected is Data) {
                    // if (modelSelected.isItCurrent) {
                    // initSlider(modelSelected.pictures)
                    // }
                    //model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
                    Log.d("paretnId" , "observeParent $modelSelected")

                    val bundle = Bundle()
                    //bundle.putInt(EXITENCEIDPACKAGE, availableServiceList.get(position).id?:-1)
                    bundle.putInt(PARENT_ID, modelSelected.id?:-1)
                    bundle.putString(PARENT_NAME, modelSelected.name?:"")
                    bundle.putInt(WHICH_ADD_PRD_STORE , arguments?.getInt(WHICH_ADD_PRD_STORE)?:R.id.frameLayout_direction)

                        UtilKotlin.changeFragmentWithBack(
                            activity!!,
                            arguments?.getInt(WHICH_ADD_PRD_STORE)?:R.id.frameLayout_direction,
                            FragmentSubProductclassificationCenter(),
                            bundle)




                }
                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null

            }

            Log.d("paretnId" , "j22j")
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

    companion object{

        var PARENT_ID = "parentId"
        var SUB_PARENT_ID = "subParentId"
        var SUB_PARENT_NAME = "subParentName"
        var PARENT_NAME = "parentName"
        var ISCALLCENTER= "Call_center"

    }
}