package com.smartangle.controllersystemapp.accountant.createdebts

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.products.AccountantProductPresenter
import com.smartangle.controllersystemapp.accountant.products.models.AccountantProductsListResponse
import com.smartangle.controllersystemapp.accountant.products.models.Data
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.AccountantProductsListAdapter
import kotlinx.android.synthetic.main.fragment_debts_choose_product.*


class DebtsChooseProductFragment : Fragment() {


    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog


    lateinit var accountantProductsListAdapter: AccountantProductsListAdapter
    var productList = ArrayList<Data>()

    lateinit var rootView: View

    var nameSearch: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_debts_choose_product, container, false)
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageProduct?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
//        produtName?.text =
//            arguments?.getString(AccountantCategoriesFragment.ACC_CATEGORY_LAST_PARENT_NAME) ?: ""

        handleSearchEidtTextCLick()
        searchProdImage?.setOnClickListener {
            moveToSearch()
        }



        setViewModelListener()
    }

    private fun handleSearchEidtTextCLick() {

        searchProductEdt?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // makeSearch(search_edit_text!!.text.toString())
                    UtilKotlin.hideKeyboardEditText(searchProductEdt, rootView)
                    moveToSearch()

                    return true
                }
                return false
            }
        })

    }

    private fun moveToSearch() {

        if (!searchProductEdt?.text?.toString().isNullOrBlank()) {
            nameSearch = searchProductEdt?.text?.toString()?.trim()
            getProductsData()
        }


    }

    fun setViewModelListener() {
        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Data) { // dont set this to null because this will go to the first page

                    setAddButton(modelSelected)

                }
//                if (modelSelected is String) {
//                    UtilKotlin.showSnackErrorInto(activity!!, getString(R.string.max_quantity))
//                    selectAccProductBtn?.visibility = View.GONE
//                }
                model?.setNotifyItemSelected(null)
            }
        })

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AccountantProductsListResponse) {
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

    private fun setRecycleViewData(accountantProductsListResponse: AccountantProductsListResponse) {

        if (accountantProductsListResponse?.data?.isNullOrEmpty() == false) {

            productList.clear()
            productList.addAll(accountantProductsListResponse?.data)
            accountantProductsListAdapter = AccountantProductsListAdapter(model, productList)
            UtilKotlin.setRecycleView(
                accountantProductsRecycler, accountantProductsListAdapter!!,
                RecyclerView.VERTICAL, context!!, null, true
            )

        } else {
            //empty
            Log.d("productDelegate", "empty")
//            productList.clear()
//            productList.add(Data("" , null , 1 , "" ,
//                "" , 55 , "" , "iphone6s" , "" , "81" , null))
//            delegateProductsListAdapter = DelegateProductsListAdapter(model, productList)
//            UtilKotlin.setRecycleView(
//                delegateProductsRecycler, delegateProductsListAdapter!!,
//                RecyclerView.VERTICAL, context!!, null, true
//            )
        }


    }

    override fun onResume() {
        super.onResume()

        getProductsData()
    }

    private fun getProductsData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantProductPresenter.productsList(
                webService!!,
                0,
                nameSearch,
                activity!!,
                model
            )


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun setAddButton(modelSelected: Data) {
        selectAccProductBtn?.visibility = View.VISIBLE
        selectAccProductBtn?.setOnClickListener {
            /* val index = activity!!.supportFragmentManager.backStackEntryCount - 1
              val lastEntry: FragmentManager.BackStackEntry =
                  activity!!.supportFragmentManager!!.getBackStackEntryAt(index)
              val previous: FragmentManager.BackStackEntry =
                  activity!!.supportFragmentManager!!.getBackStackEntryAt(index - 3)
              activity!!.supportFragmentManager.popBackStack(previous.id,0)*/

            if (modelSelected.selectedQuantity ?: 0 > (modelSelected.total_quantity?.toInt()) ?: 0)
                UtilKotlin.showSnackErrorInto(activity!!, getString(R.string.max_quantity))
            else
            {
                model?.responseCodeDataSetter(modelSelected)
                model?.setNotifyItemSelected(null)

                //model?.responseCodeDataSetter(arguments?.getInt(FragmentProductclassification.PARENT_ID)?:-1)
                activity?.supportFragmentManager?.popBackStack()

            }

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

}