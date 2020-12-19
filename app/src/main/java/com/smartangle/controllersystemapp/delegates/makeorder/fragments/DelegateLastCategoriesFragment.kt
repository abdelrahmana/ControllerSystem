package com.smartangle.controllersystemapp.delegates.makeorder.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.smartangle.controllersystemapp.admin.categories.models.Data
import com.smartangle.controllersystemapp.delegates.makeorder.DelegateMakeOrderPresenter
import com.smartangle.controllersystemapp.delegates.makeorder.adapter.DelegatelastCategoriesAdaptor
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_delegate_categories.*

class DelegateLastCategoriesFragment : Fragment() {


    lateinit var model : ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    var delegatelastCategoriesAdaptor : DelegatelastCategoriesAdaptor?=null
    var lastSubCategoryList = ArrayList<Data>()

    var nameSearch : String? = null
    var parentId : Int ? = null
    lateinit var rootView : View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_delegate_categories, container, false)
        // Inflate the layout for this fragment
        model =UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        parentId = arguments?.getInt(DelegateCategoriesFragment.CATEGORY_SUB_PARENT_ID)?:0

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subCategroyHeader?.text = arguments?.getString(DelegateCategoriesFragment.CATEGORY_SUB_PARENT_NAME)?:""
        backIconn?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
        handleSearchEidtTextCLick()

        search_arrow_image?.setOnClickListener {
            moveToSearch()
        }
           //setRecycleViewData() // set recycleView
        setViewModelListener() // last item clicked now we need to go to the add product fragment and no back please
       // activity!!.supportFragmentManager.popBackStack()
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
            getLastSubData()
        }

    }

    override fun onResume() {
        super.onResume()

        getLastSubData()
    }

    private fun getLastSubData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            DelegateMakeOrderPresenter.delegateCategoriesList(webService!!, parentId , nameSearch , activity!!, model)


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

        }

    }
    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {

        if (categoriesListResponse.data?.isNullOrEmpty() == false) {

            lastSubCategoryList.clear()
            lastSubCategoryList.addAll(categoriesListResponse?.data)
            delegatelastCategoriesAdaptor = DelegatelastCategoriesAdaptor(model, lastSubCategoryList)
            UtilKotlin.setRecycleView(delegateCategoriesRecycler, delegatelastCategoriesAdaptor!!,
                RecyclerView.VERTICAL,context!!, null, true)

        } else {
            //empty
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
                    Log.d("finalDataSelect", "id ${modelSelected.id?:-1}")
                  //  setAddButton(modelSelected)
                    val bundle = Bundle()
                    bundle.putInt(DelegateCategoriesFragment.CATEGORY_LAST_PARENT_ID, modelSelected.id?:-1)
                    bundle.putString(DelegateCategoriesFragment.CATEGORY_LAST_PARENT_NAME, modelSelected.name?:"")
                    UtilKotlin.changeFragmentWithBack(activity!! ,
                        R.id.frameLayoutDirdelegate,
                        DelegateProductsFragment() , bundle)
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

//    private fun setAddButton(modelSelected: Data) {
//        addProductButton?.visibility = View.VISIBLE
//        addProductButton?.setOnClickListener{
//          /* val index = activity!!.supportFragmentManager.backStackEntryCount - 1
//            val lastEntry: FragmentManager.BackStackEntry =
//                activity!!.supportFragmentManager!!.getBackStackEntryAt(index)
//            val previous: FragmentManager.BackStackEntry =
//                activity!!.supportFragmentManager!!.getBackStackEntryAt(index - 3)
//            activity!!.supportFragmentManager.popBackStack(previous.id,0)*/
//            Log.d("addBtn" , "last ${arguments?.getInt(FragmentProductclassification.PARENT_ID)}")
//            Log.d("addBtn" , "parentName ${arguments?.getString(FragmentProductclassification.PARENT_NAME)}")
//            Log.d("addBtn" , "subName ${arguments?.getString(FragmentProductclassification.SUB_PARENT_NAME)}")
//            Log.d("addBtn" , "lastName ${modelSelected?.name?:""}")
//
//            model?.responseCodeDataSetter(ViewModelHandleChangeFragmentclass.ProductClassification(
//                arguments?.getInt(FragmentProductclassification.PARENT_ID)?:-1,
//                arguments?.getString(FragmentProductclassification.PARENT_NAME)?:"",
//                arguments?.getString(FragmentProductclassification.SUB_PARENT_NAME)?:"",
//                modelSelected?.name?:""
//            ))
//
//            //model?.responseCodeDataSetter(arguments?.getInt(FragmentProductclassification.PARENT_ID)?:-1)
//            activity!!.supportFragmentManager.popBackStack()
//            activity!!.supportFragmentManager.popBackStack()
//            activity!!.supportFragmentManager.popBackStack()
//
//        }
//    }

}