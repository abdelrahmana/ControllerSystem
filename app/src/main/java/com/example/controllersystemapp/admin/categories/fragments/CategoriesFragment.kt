package com.example.controllersystemapp.admin.categories.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.CategoriesPresenter
import com.example.controllersystemapp.admin.categories.adapters.CategoriesAdapter
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.delegatesAccountants.AccountantPresenter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() , OnRecyclerItemClickListener {

    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog


    lateinit var categoriesAdapter: CategoriesAdapter
    var categoryList = ArrayList<Data>()
    lateinit var rootView: View

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


        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(categoriesRecycler)


        back?.setOnClickListener {

            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }


        observeData()
    }

    var removePosition = 0

    var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int
        ) {
            //Remove swiped item from list and notify the RecyclerView
            val position = viewHolder.adapterPosition

            position?.let {
                removePosition = it
                removeCategoryItem(it)

            }
        }
    }

    private fun removeCategoryItem(position: Int) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            CategoriesPresenter.deleteCategoryPresenter(webService!! ,
                categoryList[position].id?:-1 , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }


    private fun observeData() {

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
                    successRemove(datamodel)
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

    private fun successRemove(successModel: SuccessModel) {

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

            categoriesAdapter = CategoriesAdapter(categoryList , this)
            categoriesRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = categoriesAdapter
            }

        }
        else{
            //emoty
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


    override fun onItemClick(position: Int) {
        Log.d("click" , "category")
    }

}