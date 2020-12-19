package com.smartangle.controllersystemapp.accountant.makeorder.fragments

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
import com.smartangle.controllersystemapp.accountant.makeorder.AccountantMakeOrderPresenter
import com.smartangle.controllersystemapp.accountant.makeorder.adapters.AccountantSubCategoriesAdaptor
import com.smartangle.controllersystemapp.accountant.makeorder.fragments.AccountantCategoriesFragment.Companion.ACC_CATEGORY_SUB_PARENT_ID
import com.smartangle.controllersystemapp.accountant.makeorder.fragments.AccountantCategoriesFragment.Companion.ACC_CATEGORY_SUB_PARENT_NAME
import com.smartangle.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.smartangle.controllersystemapp.admin.categories.models.Data
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_accountant_categories.*


class AccountantSubCategoryFragment : Fragment() {

    lateinit var model : ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    lateinit var accountantSubCategoriesAdaptor: AccountantSubCategoriesAdaptor
    var subCategoryList = ArrayList<Data>()

    var nameSearch : String? = null
    var parentId : Int ? = null
    lateinit var rootView : View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_accountant_categories, container, false)
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        parentId = arguments?.getInt(AccountantCategoriesFragment.ACC_CATEGORY_PARENT_ID)?:0

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subCategroyHeader?.text = arguments?.getString(AccountantCategoriesFragment.ACC_CATEGORY_PARENT_NAME)?:""
        backIconn?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
        handleSearchEidtTextCLick()

        search_arrow_image?.setOnClickListener {
            moveToSearch()
        }
        //setRecycleViewData() // set recycleView
        setViewModelListener() // when select item
    }

    private fun handleSearchEidtTextCLick() {

        searchCategory?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // makeSearch(search_edit_text!!.text.toString())
                    UtilKotlin.hideKeyboardEditText(searchCategory , rootView)
                    moveToSearch()

                    return true
                }
                return false
            }
        })

    }

    private fun moveToSearch() {

        if (!searchCategory?.text?.toString().isNullOrBlank())
        {
            nameSearch = searchCategory?.text?.toString()?.trim()
            getSubData()

        }


    }


    override fun onResume() {
        super.onResume()

        getSubData()
    }

    private fun getSubData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantMakeOrderPresenter.accountantCategoriesList(webService!!, parentId , nameSearch , activity!!, model)


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {

        if (categoriesListResponse.data?.isNullOrEmpty() == false) {

            subCategoryList.clear()
            subCategoryList.addAll(categoriesListResponse?.data)
            accountantSubCategoriesAdaptor = AccountantSubCategoriesAdaptor(model, subCategoryList)
            UtilKotlin.setRecycleView(accountantCategoriesRecycler, accountantSubCategoriesAdaptor!!,
                RecyclerView.VERTICAL,context!!, null, true)
        } else {
            //empty
        }

    }

    fun setViewModelListener() {
        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Data) {

                    val bundle = Bundle()
                    bundle.putInt(ACC_CATEGORY_SUB_PARENT_ID, modelSelected.id?:-1)
                    bundle.putString(ACC_CATEGORY_SUB_PARENT_NAME, modelSelected.name?:"")
                    UtilKotlin.changeFragmentWithBack(activity!! ,
                        R.id.redirect_acc_fragments,
                        AccountantLastCategoriesFragment() , bundle)
                }
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