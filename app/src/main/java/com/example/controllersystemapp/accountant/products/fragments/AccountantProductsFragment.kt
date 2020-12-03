package com.example.controllersystemapp.accountant.products.fragments

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
import com.example.controllersystemapp.accountant.products.AccountantProductPresenter
import com.example.controllersystemapp.accountant.products.models.AccountantProductsListResponse
import com.example.controllersystemapp.accountant.products.models.Data
import com.example.controllersystemapp.accountant.products.adapters.AccountantProductsAdapter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_accountant_products.*


class AccountantProductsFragment : Fragment(), OnRecyclerItemClickListener {


    var productsList = ArrayList<Data>()
    lateinit var accountantProductsAdapter: AccountantProductsAdapter

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

        return inflater.inflate(R.layout.fragment_accountant_products, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backIcon?.setOnClickListener {

            activity?.let {
                if (it.supportFragmentManager.backStackEntryCount == 1)
                {
                    it.finish()
                }
                else{
                    it.supportFragmentManager.popBackStack()
                }
            }

        }


        observeData()

    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AccountantProductsListResponse) {
                    Log.d("testApi", "isForyou")
                    getProductsData(datamodel)
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


    }

    private fun getProductsData(accountantProductsListResponse: AccountantProductsListResponse) {

        if (accountantProductsListResponse?.data?.isNullOrEmpty() == false)
        {
            accProdRecycler?.visibility = View.VISIBLE
            noProductData?.visibility = View.GONE

            productsList.clear()
            productsList.addAll(accountantProductsListResponse?.data)
            accountantProductsAdapter =
                AccountantProductsAdapter(
                    context!!,
                    productsList,
                    this
                )
            accProdRecycler?.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = accountantProductsAdapter
            }

        }else{

            //empty
            accProdRecycler?.visibility = View.GONE
            noProductData?.visibility = View.VISIBLE

        }


    }

    override fun onResume() {
        super.onResume()

        getData()
    }

    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantProductPresenter.productsList(
                webService!!,
                null , null ,
                activity!!,
                model
            )

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    override fun onItemClick(position: Int) {

        val bundle = Bundle()
        bundle.putInt(ACC_PROD_ID, productsList[position].id?:0)
        UtilKotlin.changeFragmentBack(activity!! ,
            AccProdDetailsFragment(), ""  ,
            bundle , R.id.redirect_acc_fragments)
    }


    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }


    companion object{

        val ACC_PROD_ID = "accountantproductId"
    }
}