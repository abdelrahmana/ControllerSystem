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
import com.smartangle.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.smartangle.controllersystemapp.admin.categories.models.Data
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.AccountantCategoriesAdaptor
import kotlinx.android.synthetic.main.fragment_accountant_categories.*



class AccountantCategoriesFragment : Fragment() {

    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog
    lateinit var rootView : View

    lateinit var accountantCategoriesAdaptor : AccountantCategoriesAdaptor
    var categoriesParentList = ArrayList<Data>()

    var nameSearch : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_accountant_categories, container, false)
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        // Inflate the layout for this fragment
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backIconn?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
        handleSearchEidtTextCLick()

        search_arrow_image?.setOnClickListener {
            moveToSearch()
        }

        setViewModelListener()
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
            getData()
        }


    }


    override fun onResume() {
        super.onResume()

        getData()


    }

    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantMakeOrderPresenter.accountantCategoriesList(webService!!, null, nameSearch , activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    fun setViewModelListener() {
        Log.d("paretnId" , "jj")

        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                Log.d("paretnId" , "observeParent")

                if (modelSelected is Data) {

                    val bundle = Bundle()
                    bundle.putInt(ACC_CATEGORY_PARENT_ID, modelSelected.id?:-1)
                    bundle.putString(ACC_CATEGORY_PARENT_NAME, modelSelected.name?:"")

                    UtilKotlin.changeFragmentWithBack(
                        activity!!,
                        R.id.redirect_acc_fragments,
                        AccountantSubCategoryFragment(),
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

    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {


        if (categoriesListResponse.data?.isNullOrEmpty() == false) {

            categoriesParentList.clear()
            categoriesParentList.addAll(categoriesListResponse?.data)
            accountantCategoriesAdaptor = AccountantCategoriesAdaptor(model, categoriesParentList)
            UtilKotlin.setRecycleView(
                accountantCategoriesRecycler, accountantCategoriesAdaptor!!,
                RecyclerView.VERTICAL, context!!, null, true
            )
        } else {
            //empty
        }

    }


    override fun onDestroyView() {
        model?.notifyItemSelected?.removeObservers(activity!!)
        model?.responseDataCode?.removeObservers(activity!!)
        model?.errorMessage?.removeObservers(activity!!)

        super.onDestroyView()
    }

    companion object{

        var ACC_CATEGORY_PARENT_ID = "accCategoryParentId"
        var ACC_CATEGORY_SUB_PARENT_ID = "accCategorySubParentId"
        var ACC_CATEGORY_SUB_PARENT_NAME = "accCategorySubParentName"
        var ACC_CATEGORY_PARENT_NAME = "accCategoryParentName"
        var ACC_CATEGORY_LAST_PARENT_ID = "accCategoryLastParentId"
        var ACC_CATEGORY_LAST_PARENT_NAME = "accCategoryLastParentName"

    }

}