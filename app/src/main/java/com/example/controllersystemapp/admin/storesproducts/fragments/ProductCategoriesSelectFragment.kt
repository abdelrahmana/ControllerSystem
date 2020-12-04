package com.example.controllersystemapp.admin.storesproducts.fragments

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
import com.example.controllersystemapp.admin.interfaces.OnCategoriesSelectedelcteClickListener
import com.example.controllersystemapp.admin.storesproducts.models.NamesIdModel
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.ProductCategoriesSelectAdaptor
import kotlinx.android.synthetic.main.fragment_product_categories_select.*


class ProductCategoriesSelectFragment : Fragment()  , OnCategoriesSelectedelcteClickListener {

    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog


    var productAdaptor: ProductCategoriesSelectAdaptor? = null
    var categoriesParentList = ArrayList<Data>()

    var categoriesFinalList = ArrayList<Int>()
    var categoriesNamesFinalList = ArrayList<String>()
    val selectedDataList: ArrayList<Data> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_product_categories_select, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCategoriesButton?.setOnClickListener {

            Log.d("getData" , "id ${categoriesFinalList.size}")
            Log.d("getData" , "name ${categoriesNamesFinalList.size}")

            model.responseCodeDataSetter(NamesIdModel(categoriesFinalList , categoriesNamesFinalList))
            activity?.supportFragmentManager?.popBackStack()

        }

        backButtonCategorieds?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        setViewModelListener()

    }

    fun setViewModelListener() {

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

    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {

        if (categoriesListResponse.data?.isNullOrEmpty() == false) {

            categoriesParentList.clear()
            categoriesParentList.addAll(categoriesListResponse?.data)
            productAdaptor = ProductCategoriesSelectAdaptor(model, categoriesParentList , this)
            UtilKotlin.setRecycleView(
                productCategoriesList , productAdaptor!!,
                RecyclerView.VERTICAL, context!!, null, true
            )
        } else {
            //empty
        }

    }

    override fun onResume() {
        super.onResume()

        getData()


    }

    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenter.getCategoriesList(webService!!, null, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

//    override fun onClickItemClick(
//        position: Int,
//        categoriesList: ArrayList<Int>,
//        categoriesListNames: ArrayList<String>
//    ) {
//
//        categoriesFinalList.clear()
//        for (i in categoriesList.indices) {
//
//            categoriesFinalList?.add(categoriesList[i])
//            Log.d("clickData" , "quantity ${categoriesList?.get(i)}")
//        }
//
//        categoriesNamesFinalList.clear()
//        for (i in categoriesListNames.indices) {
//            categoriesNamesFinalList?.add(categoriesListNames[i])
//        }
//
//
//
//    }

    override fun onItemClickData(position: Int, categoriesList: ArrayList<Data>) {

        addCategoriesButton?.visibility = View.VISIBLE
        categoriesFinalList.clear()
        categoriesNamesFinalList.clear()

        if (categoriesList?.size?:0 > 0) {
            for (i in 0 until categoriesList?.size!!) {
                categoriesFinalList.add(categoriesList?.get(i)?.id?:0)
                categoriesNamesFinalList.add(categoriesList?.get(i)?.name?:"")
                // quantityList.add(storedAdapter?.getSelected()?.get(i)?.quantity?:0)
            }

        } else {
            addCategoriesButton?.visibility = View.GONE
            Log.d("finalResult" , "NoSelection")
        }

    }

    override fun onDestroyView() {
        model?.responseDataCode?.removeObservers(activity!!)
        model?.errorMessage?.removeObservers(activity!!)

        super.onDestroyView()
    }


}