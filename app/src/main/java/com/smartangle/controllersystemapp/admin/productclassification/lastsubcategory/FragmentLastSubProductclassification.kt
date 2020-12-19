package com.smartangle.controllersystemapp.admin.productclassification.lastsubcategory

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.categories.CategoriesPresenter
import com.smartangle.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.smartangle.controllersystemapp.admin.categories.models.Data
import com.smartangle.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.smartangle.controllersystemapp.admin.productclassification.FragmentProductclassification.Companion.SUB_PARENT_NAME
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_product_classification.*

class FragmentLastSubProductclassification : Fragment() {


    lateinit var model : ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    var subProductAdaptor : LastSubProductClassificationAdaptor?=null
    var lastSubCategoryList = ArrayList<Data>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model =UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_product_classification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subCategroyHeader?.text = arguments?.getString(SUB_PARENT_NAME)?:""
        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
           //setRecycleViewData() // set recycleView
        setViewModelListener() // last item clicked now we need to go to the add product fragment and no back please
       // activity!!.supportFragmentManager.popBackStack()
    }


    override fun onResume() {
        super.onResume()

        getLastSubData()
    }

    private fun getLastSubData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            CategoriesPresenter.getCategoriesList(webService!!
                , arguments?.getInt(FragmentProductclassification.SUB_PARENT_ID)?:-1
                , activity!!, model)

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
            it?.errorMessage?.removeObservers(activity!!)

        }

    }
    private fun setRecycleViewData(categoriesListResponse: CategoriesListResponse) {

        if (categoriesListResponse.data?.isNullOrEmpty() == false) {

            lastSubCategoryList.clear()
            lastSubCategoryList.addAll(categoriesListResponse?.data)
            subProductAdaptor = LastSubProductClassificationAdaptor(model, lastSubCategoryList)
            UtilKotlin.setRecycleView(productList,subProductAdaptor!!,
                RecyclerView.VERTICAL,context!!, null, true)

        } else {
            //empty
        }




//        val arrayList = ArrayList<Any>()
//        arrayList.add("العاب")
//        arrayList.add("منزل")
//        arrayList.add("اجهزة منزلية")
//        subProductAdaptor = LastSubProductClassificationAdaptor(model,arrayList)
//        UtilKotlin.setRecycleView(productList,subProductAdaptor!!,
//            RecyclerView.VERTICAL,context!!, null, true)
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

                    setAddButton(modelSelected)
                }
                /* else if (modelSelected is ImageModelData) // if it is object of this model
                  {
                      /*  val pictures = ArrayList<Picture>()
                        datamodel.image?.forEach{
                            pictures.add(Picture(it))

                        }*/

                      //  initSlider(pictures) // add these services to image
                      //getData(datamodel) // move data to here please
                  }
  */

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

    private fun setAddButton(modelSelected: Data) {
        addProductButton?.visibility = View.VISIBLE
        addProductButton?.setOnClickListener{
          /* val index = activity!!.supportFragmentManager.backStackEntryCount - 1
            val lastEntry: FragmentManager.BackStackEntry =
                activity!!.supportFragmentManager!!.getBackStackEntryAt(index)
            val previous: FragmentManager.BackStackEntry =
                activity!!.supportFragmentManager!!.getBackStackEntryAt(index - 3)
            activity!!.supportFragmentManager.popBackStack(previous.id,0)*/
//            Log.d("addBtn" , "last ${arguments?.getInt(FragmentProductclassification.PARENT_ID)}")
//            Log.d("addBtn" , "parentName ${arguments?.getString(FragmentProductclassification.PARENT_NAME)}")
//            Log.d("addBtn" , "subName ${arguments?.getString(FragmentProductclassification.SUB_PARENT_NAME)}")
//            Log.d("addBtn" , "lastName ${modelSelected?.name?:""}")

            model?.responseCodeDataSetter(ViewModelHandleChangeFragmentclass.ProductClassification(
                arguments?.getInt(FragmentProductclassification.PARENT_ID)?:-1,
                modelSelected?.id?:-1,
                arguments?.getString(FragmentProductclassification.PARENT_NAME)?:"",
                arguments?.getString(FragmentProductclassification.SUB_PARENT_NAME)?:"",
                modelSelected?.name?:""
            ))

            //model?.responseCodeDataSetter(arguments?.getInt(FragmentProductclassification.PARENT_ID)?:-1)
            activity!!.supportFragmentManager.popBackStack()
            activity!!.supportFragmentManager.popBackStack()
            activity!!.supportFragmentManager.popBackStack()

        }
    }

}