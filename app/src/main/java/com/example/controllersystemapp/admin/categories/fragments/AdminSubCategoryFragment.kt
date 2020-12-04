package com.example.controllersystemapp.admin.categories.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.CategoriesPresenter
import com.example.controllersystemapp.admin.categories.adapters.AdminSubCategoriesAdaptor
import com.example.controllersystemapp.admin.categories.fragments.CategoriesFragment.Companion.ADMINCATEG_SUB_PARENT_ID
import com.example.controllersystemapp.admin.categories.fragments.CategoriesFragment.Companion.ADMINCATEG_SUB_PARENT_NAME
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.bottomsheets.EditDeleteBottomSheet
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_admin_sub_category.*

class AdminSubCategoryFragment : Fragment() , OnRecyclerItemClickListener {

    lateinit var model : ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    lateinit var  adminSubCategoriesAdaptor : AdminSubCategoriesAdaptor
    var subCategoryList = ArrayList<Data>()

    var nameSearch : String? = null
    var parentId : Int ? = null
    var listPosition: Int ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        parentId = arguments?.getInt(CategoriesFragment.ADMINCATEG_PARENT_ID)?:0

        return inflater.inflate(R.layout.fragment_admin_sub_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        subCategoryNameText?.text = arguments?.getString(CategoriesFragment.ADMINCATEG_PARENT_NAME)?:""

        addSubCategoryBtn?.setOnClickListener {
            showAddDialog(null , null)
        }
        setRecyclerData()

        setViewModelListener() // when select item

    }

    private fun setRecyclerData() {
        adminSubCategoriesAdaptor = AdminSubCategoriesAdaptor(model, subCategoryList , this)
        UtilKotlin.setRecycleView(adminSubCategoriesRecycler, adminSubCategoriesAdaptor,
            RecyclerView.VERTICAL,context!!, null, true)
    }

    override fun onResume() {
        super.onResume()

        getSubData()
    }

    private fun getSubData(){
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            CategoriesPresenter.getCategoriesList(webService!!, parentId, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
    }

    private fun showAddDialog(editCategoryName : String? , categoryId: Int?) {


        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_category_dialog)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        dialog.window!!.setLayout(
            SlidingPaneLayout.LayoutParams.MATCH_PARENT,
            SlidingPaneLayout.LayoutParams.WRAP_CONTENT
        )
        val categoryName: EditText = dialog.findViewById(R.id.categoryNameEditTxt) as EditText
        val addDialogButton: MaterialButton = dialog.findViewById(R.id.addBtn) as MaterialButton
        val updateDialogButton: MaterialButton = dialog.findViewById(R.id.updateBtn) as MaterialButton

        if (editCategoryName != null && categoryId!= null)
        {
            updateDialogButton?.visibility = View.VISIBLE
            addDialogButton?.visibility = View.GONE
            categoryName.setText(editCategoryName)
        }

        addDialogButton?.setOnClickListener(View.OnClickListener {
            Log.d("categoryName" , "name ${categoryName?.text}")

            if (categoryName?.text?.isNotEmpty() == true)
            {
                dialog.dismiss()
                addCategory(categoryName?.text.toString()?:"")
            }
        })

        updateDialogButton?.setOnClickListener(View.OnClickListener {
            Log.d("categoryNameUpdate" , "name ${categoryName?.text}")

            if (categoryName?.text?.isNotEmpty() == true)
            {
                dialog.dismiss()
                updateCategoryData(categoryName?.text.toString()?:"" , categoryId?:-1)
            }
        })

        dialog.show()

    }

    private fun updateCategoryData(name: String, id: Int) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenter.updateCategory(webService!! , name , id , parentId , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
    }

    private fun addCategory(name : String?) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenter.addCategory(webService!! ,
                name?:"" , parentId , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    fun setViewModelListener() {
        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Data) {

                    val bundle = Bundle()
                    bundle.putInt(ADMINCATEG_SUB_PARENT_ID, modelSelected.id?:-1)
                    bundle.putString(ADMINCATEG_SUB_PARENT_NAME, modelSelected.name?:"")

                    UtilKotlin.changeFragmentWithBack(activity!! ,
                        R.id.frameLayout_direction,
                        AdminLastCategoryFragment() , bundle)
                }
                model?.setNotifyItemSelected(null)
            }
        })

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")
            Log.d("testttttD" , "datatObserve")
            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is CategoriesListResponse) {
                    Log.d("testttttD" , "datatCTEGOAry")
                    setRecycleViewData(datamodel)

                }
                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successData(datamodel)
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

        model.updateOrDelete?.observe(viewLifecycleOwner, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                setUpdateOrDelete(datamodel)
                model.setUpdateOrDelete(null) // start details with this data please
            }

        })


    }

    private fun setUpdateOrDelete(optionSelect: String) {

        when(optionSelect)
        {
            NameUtils.DELETE -> {
                Log.d("testEdit" , "delete $listPosition")
                listPosition?.let {
                    removeCategoryItem(subCategoryList[it].id?:-1)
                }
            }

            NameUtils.UPDATE -> {
                listPosition?.let {
                    showAddDialog(subCategoryList[it].name?:"" ,subCategoryList[it].id?:-1 )
                }

            }
        }

    }

    private fun removeCategoryItem(categoryId: Int) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenter.deleteCategoryPresenter(webService!! ,
                categoryId?:-1 , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun successData(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            activity?.let {
                UtilKotlin.showSnackMessage(it,successModel?.msg[0])
            }

            getSubData()
        }


    }

    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {

        if (categoriesListResponse.data?.isNullOrEmpty() == false) {
            Log.d("testttttD" , "datatNotEmpty")
            subCategoryList.clear()
            subCategoryList.addAll(categoriesListResponse?.data)
            adminSubCategoriesAdaptor?.notifyDataSetChanged()
            // adminSubCategoriesAdaptor = AdminSubCategoriesAdaptor(model, subCategoryList , this)
//            UtilKotlin.setRecycleView(adminSubCategoriesRecycler, adminSubCategoriesAdaptor,
//                RecyclerView.VERTICAL,context!!, null, true)
        } else {
            //empty
            Log.d("testttttD" , "datatEmpty")
            subCategoryList.clear()
            adminSubCategoriesAdaptor?.notifyDataSetChanged()

        }

    }

    override fun onDestroyView() {
        model?.notifyItemSelected?.removeObservers(activity!!)
        model?.responseDataCode?.removeObservers(activity!!)
        model?.errorMessage?.removeObservers(activity!!)
        model?.updateOrDelete?.removeObservers(activity!!)

        super.onDestroyView()
    }

    override fun onItemClick(position: Int) {

        listPosition = position
        val addPhotoBottomDialogFragment: EditDeleteBottomSheet =
            EditDeleteBottomSheet.newInstance()
//        val bundle = Bundle()
//        bundle.putInt(EXPENSES_ID , position)
//        addPhotoBottomDialogFragment.arguments = bundle
        addPhotoBottomDialogFragment.show(
            activity?.supportFragmentManager!!,
            EditDeleteBottomSheet.TAG
        )

    }

}