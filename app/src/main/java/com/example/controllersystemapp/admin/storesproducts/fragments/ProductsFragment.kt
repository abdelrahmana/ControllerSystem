package com.example.controllersystemapp.admin.storesproducts.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.admin.storesproducts.ProductsPresenter
import com.example.controllersystemapp.admin.storesproducts.adapters.ProductsAdapter
import com.example.controllersystemapp.admin.storesproducts.models.Data
import com.example.controllersystemapp.admin.storesproducts.models.ProductsListResponse
import com.example.controllersystemapp.admin.storesproducts.models.ProductsModel
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : Fragment()  , OnRecyclerItemClickListener {


    lateinit var rootView: View
    var productList = ArrayList<Data>()
    lateinit var productsAdapter: ProductsAdapter


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_products, container, false)
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)


        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()


    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ProductsListResponse) {
                    Log.d("testApi", "isForyou")
                    getProductsData(datamodel)
                }
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


    }

    override fun onResume() {
        super.onResume()


        getProductsRequest()


        //getProductsData()


    }

    private fun getProductsRequest() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ProductsPresenter.getProductsList(webService!! , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }




    }

    private fun getProductsData(productsListResponse: ProductsListResponse) {

        if (productsListResponse?.data?.isNullOrEmpty() == false)
        {
            productsRecycler.visibility = View.VISIBLE
            noProductData.visibility = View.GONE

            productList.clear()
            productList.addAll(productsListResponse?.data)
            productsAdapter = ProductsAdapter(context!! , productList , this)

            productsRecycler?.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = productsAdapter

            }


        }else{

            //empty
            productsRecycler.visibility = View.GONE
            noProductData.visibility = View.VISIBLE

        }

    }

    override fun onItemClick(position: Int) {

        //Toast.makeText(context!! , "pos "+productList[position].name , Toast.LENGTH_LONG).show()
        Log.d("click" , "position $position name ${productList[position]}")

    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }


}