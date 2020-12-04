package com.example.controllersystemapp.delegates.makeorder.fragments

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
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.delegates.makeorder.DelegateMakeOrderPresenter
import com.example.controllersystemapp.delegates.makeorder.adapter.DelegatelastCategoriesAdaptor
import com.example.controllersystemapp.delegates.makeorder.model.Data
import com.example.controllersystemapp.delegates.makeorder.model.DelegateProductsListResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.DelegateProductsListAdapter
import kotlinx.android.synthetic.main.fragment_delegate_products.*


class DelegateProductsFragment : Fragment() {

    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    var productList = ArrayList<Data>()
    lateinit var delegateProductsListAdapter: DelegateProductsListAdapter

    lateinit var rootView : View

    var nameSearch: String? = null
    var categoryId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_delegate_products, container, false)
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        categoryId = arguments?.getInt(DelegateCategoriesFragment.CATEGORY_LAST_PARENT_ID) ?: 0

        // Inflate the layout for this fragment
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageProduct?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        productCategoryName?.text =
            arguments?.getString(DelegateCategoriesFragment.CATEGORY_LAST_PARENT_NAME) ?: ""

        handleSearchEidtTextCLick()
        searchProdImage?.setOnClickListener {
            moveToSearch()
        }



        setViewModelListener()
    }

    private fun handleSearchEidtTextCLick() {

        searchProduct?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // makeSearch(search_edit_text!!.text.toString())
                    UtilKotlin.hideKeyboardEditText(searchProduct , rootView)
                    moveToSearch()

                    return true
                }
                return false
            }
        })

    }

    private fun moveToSearch() {

        if (!searchProduct?.text?.toString().isNullOrBlank())
        {
            nameSearch = searchProduct?.text?.toString()?.trim()
            getProductsData()
        }


    }


    fun setViewModelListener() {
        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Data) { // dont set this to null because this will go to the first page
                    // if (modelSelected.isItCurrent) {
                    // initSlider(modelSelected.pictures)
                    // }
                    /*        model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
                            val bundle = Bundle()
                            //     bundle.putInt(EXITENCEIDPACKAGE,availableServiceList.get(position).id?:-1)
                            UtilKotlin.changeFragmentWithBack(activity!! , R.id.container , FragmentLastSubProductclassification() , bundle)*/
                    Log.d("finalDataSelect", "id ${modelSelected.id ?: -1}")
                    setAddButton(modelSelected)
//                    val bundle = Bundle()
//                    bundle.putInt(DelegateCategoriesFragment.CATEGORY_LAST_PARENT_ID, modelSelected.id?:-1)
//                    bundle.putString(DelegateCategoriesFragment.CATEGORY_LAST_PARENT_NAME, modelSelected.name?:"")
//                    UtilKotlin.changeFragmentWithBack(activity!! ,
//                        R.id.frameLayoutDirdelegate,
//                        DelegateProductsFragment() , bundle)
                }
                model?.setNotifyItemSelected(null)
            }
        })

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is DelegateProductsListResponse) {
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

    private fun setRecycleViewData(delegateProductsListResponse: DelegateProductsListResponse) {

        if (delegateProductsListResponse?.data?.isNullOrEmpty() == false) {

            productList.clear()
            productList.addAll(delegateProductsListResponse?.data)
            delegateProductsListAdapter = DelegateProductsListAdapter(model, productList)
            UtilKotlin.setRecycleView(
                delegateProductsRecycler, delegateProductsListAdapter!!,
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

            DelegateMakeOrderPresenter.delegateProductsList(
                webService!!,
                categoryId,
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
        confirmSelectProductBtn?.visibility = View.VISIBLE
        confirmSelectProductBtn?.setOnClickListener {
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

                //model?.responseCodeDataSetter(arguments?.getInt(FragmentProductclassification.PARENT_ID)?:-1)
                activity?.supportFragmentManager?.popBackStack()
                activity?.supportFragmentManager?.popBackStack()
                activity?.supportFragmentManager?.popBackStack()
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