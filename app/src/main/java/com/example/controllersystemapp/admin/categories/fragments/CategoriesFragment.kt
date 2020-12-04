package com.example.controllersystemapp.admin.categories.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.settings.expenses.AddAccountantExpensesFragment
import com.example.controllersystemapp.accountant.settings.expenses.ExpensesFragment
import com.example.controllersystemapp.admin.categories.CategoriesPresenter
import com.example.controllersystemapp.admin.categories.adapters.CategoriesAdapter
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.delegatesAccountants.AccountantPresenter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.bottomsheets.EditDeleteBottomSheet
import com.example.controllersystemapp.delegates.makeorder.fragments.DelegatSubCategoriesFragment
import com.example.controllersystemapp.delegates.makeorder.fragments.DelegateCategoriesFragment
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() , OnRecyclerItemClickListener {

    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog


    lateinit var categoriesAdapter: CategoriesAdapter
    var categoryList = ArrayList<Data>()
    lateinit var rootView: View
    var listPosition: Int ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        rootView = inflater.inflate(R.layout.fragment_categories, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(categoriesRecycler)


        back?.setOnClickListener {

            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        addCategoryBtn?.setOnClickListener {
            showAddDialog(null , null)
        }

        setRecyclerData()

        observeData()
    }

    private fun setRecyclerData() {

        categoriesAdapter = CategoriesAdapter(model, categoryList , this )
        categoriesRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = categoriesAdapter
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

            CategoriesPresenter.updateCategory(webService!! , name , id , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
    }

    private fun addCategory(name : String?) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenter.addCategory(webService!! ,
                name?:"" , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    var removePosition = 0

//    var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
//        0,
//        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//    ) {
//        override fun onMove(
//            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//            return false
//        }
//
//        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int
//        ) {
//            //Remove swiped item from list and notify the RecyclerView
//            val position = viewHolder.adapterPosition
//
//            position?.let {
//                removePosition = it
//                removeCategoryItem(it)
//
//            }
//        }
//    }

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


    private fun observeData() {

        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                Log.d("Click" , "observeData")

                if (modelSelected is Data) {
                    val bundle = Bundle()
                    bundle.putInt(ADMINCATEG_PARENT_ID, modelSelected.id?:-1)
                    bundle.putString(ADMINCATEG_PARENT_NAME, modelSelected.name?:"")

                    UtilKotlin.changeFragmentWithBack(
                        activity!!,
                        R.id.frameLayout_direction,
                        AdminSubCategoryFragment(),
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

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successAdd(datamodel)
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
                    removeCategoryItem(categoryList[it].id?:-1)
                }
            }

            NameUtils.UPDATE -> {
               // Log.d("testEdit" , "update $listPosition")
                listPosition?.let {
                    showAddDialog(categoryList[it].name?:"" ,categoryList[it].id?:-1 )
                }


            }
        }


    }


    private fun successAdd(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            activity?.let {
                UtilKotlin.showSnackMessage(it,successModel?.msg[0])
            }

//            productsAdapter.let {
//                it?.removeItemFromList(removePosition)
//            }
//            productsAdapter?.notifyDataSetChanged()

            getCateogriesData()
        }


    }

    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {

        if (categoriesListResponse?.data?.isNullOrEmpty() == false)
        {
            categoryList.clear()
            categoryList.addAll(categoriesListResponse?.data)
            categoriesAdapter?.notifyDataSetChanged()
//            categoriesAdapter = CategoriesAdapter(model, categoryList , this )
//            categoriesRecycler?.apply {
//                setHasFixedSize(true)
//                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
//                adapter = categoriesAdapter
//            }

        }
        else{
            //emoty
            categoryList.clear()
            categoriesAdapter?.notifyDataSetChanged()

        }


    }

    override fun onResume() {
        super.onResume()
        Log.d("back" , "Delegate Resume")

        getCateogriesData()
    }

    private fun getCateogriesData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenter.getCategoriesList(webService!!, null, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }
    companion object{

        var ADMINCATEG_PARENT_ID = "adminCatgParentId"
        var ADMINCATEG_SUB_PARENT_ID = "adminCatgSubParentId"
        var ADMINCATEG_SUB_PARENT_NAME = "adminCatgSubParentName"
        var ADMINCATEG_PARENT_NAME = "adminCatgParentName"
        var ADMINCATEG_LAST_PARENT_ID = "adminCatgLastParentId"
     //   var ADMINCATEG_LAST_PARENT_NAME = "adminCatgLastParentName"

    }

    override fun onDestroyView() {
        model?.notifyItemSelected?.removeObservers(activity!!)
        model?.responseDataCode?.removeObservers(activity!!)
        model?.errorMessage?.removeObservers(activity!!)
        model?.updateOrDelete?.removeObservers(activity!!)

        super.onDestroyView()
    }

    override fun onItemClick(position: Int) {
        Log.d("click" , "category")
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