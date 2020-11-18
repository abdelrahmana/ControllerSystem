package com.example.controllersystemapp.admin.storeclassification

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.addproduct.StoreIdQuantity
import com.example.controllersystemapp.admin.interfaces.OnStoreSelcteClickListener
import com.example.controllersystemapp.admin.storesproducts.StoresPresenter
import com.example.controllersystemapp.admin.storesproducts.models.StoresData
import com.example.controllersystemapp.admin.storesproducts.models.StoresListResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.StoredClassificationAdaptor
import kotlinx.android.synthetic.main.fragment_product_classification.*

class StoreClassificationFragment : Fragment() , OnStoreSelcteClickListener {

    lateinit var model :ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    var storedAdapter : StoredClassificationAdaptor?=null
    var storesList = ArrayList<StoresData>()

    var quantityList = ArrayList<Int>()
    var storesIdList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_classification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subCategroyHeader?.text = getString(R.string.save_store)
        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
        //setRecycleViewData()
        setViewModelListener()
        setClickListener()
    }

    override fun onResume() {
        super.onResume()

        getData()


    }

    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            StoresPresenter.getStoresList(webService!!, null, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }




    private fun setRecycleViewData(storesListResponse: StoresListResponse) {


        if (storesListResponse?.data?.isNullOrEmpty() == false)
        {
            storesList?.clear()
            storesList.addAll(storesListResponse?.data)
            storedAdapter = StoredClassificationAdaptor(model, storesList , this)
            UtilKotlin.setRecycleView(productList,storedAdapter!!,
                RecyclerView.VERTICAL,context!!, null, true)
        }
        else{
            //empty
        }

//       val arrayList = ArrayList<Any>()
//        arrayList.add("مخزن 1")
//        arrayList.add("مخزن 2")
//        arrayList.add("مخزن 3")
//        storedAdapter = StoredClassificationAdaptor(model,arrayList)
//        UtilKotlin.setRecycleView(productList,storedAdapter!!,
//            RecyclerView.VERTICAL,context!!, null, true)
    }

    override fun onDestroyView() {
      model?.notifyItemSelected?.removeObservers(activity!!)
        super.onDestroyView()

    }

    fun setViewModelListener() {
       // please add the item you want to add in arraylist here
       model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
           if (modelSelected != null) { // if null here so it's new service with no any data
               if (modelSelected is StoresData) {
                   // if (modelSelected.isItCurrent) {
                   // initSlider(modelSelected.pictures)
                   // }
                 //  val bundle = Bundle()
              //     bundle.putInt(EXITENCEIDPACKAGE,availableServiceList.get(position).id?:-1)
                //   UtilKotlin.changeFragmentWithBack(activity!! , R.id.container , FragmentSubProductclassification() , bundle)
                   addProductButton?.visibility = View.VISIBLE
                   this.modelSelected = modelSelected
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
               model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null

           }
       })


        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is StoresListResponse) {
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

    var modelSelected : Any?=null
    var selectedStoreData : ArrayList<StoresData> = ArrayList()

    private fun setClickListener() {
        addProductButton?.setOnClickListener{

            if (storedAdapter?.getSelected()?.size?:0 > 0) {
                selectedStoreData = storedAdapter?.getSelected()!!
            }

//            quantityList.clear()
//            if (storedAdapter?.getSelected()?.size?:0 > 0) {
//                for (i in 0 until storedAdapter?.getSelected()?.size!!) {
//                    quantityList.add(storedAdapter?.getSelected()?.get(i)?.quantity?:1)
//                }
//                Log.d("finalResult" , "size ${storesIdList?.size}")
//                Log.d("quantityFinalResult" , "size ${quantityList?.size}")
//
//                // showToast(stringBuilder.toString().trim { it <= ' ' })
//            } else {
//                Log.d("finalResult" , "NoSelection")
//            }
//
//            for (i in quantityList.indices) {
//                Log.d("quantityFinalResult" , " Data $i ${quantityList[i]}")
//            }
           // val storeIdQuantity = StoreIdQuantity(quantityList , storesIdList)
            //model.responseCodeDataSetter(storeIdQuantity)

            val storesListResponse = StoresListResponse(selectedStoreData)
            model.responseCodeDataSetter(storesListResponse)

            activity?.supportFragmentManager?.popBackStack()


        }
    }

    override fun onClickItemClick(
        position: Int,
        quantitiesList: ArrayList<Int>,
        storesIdsList: ArrayList<Int>
    ) {
        addProductButton?.visibility = View.VISIBLE
        storesIdList.clear()

        if (storedAdapter?.getSelected()?.size?:0 > 0) {
            for (i in 0 until storedAdapter?.getSelected()?.size!!) {
                storesIdList.add(storedAdapter?.getSelected()?.get(i)?.id?:0)
               // quantityList.add(storedAdapter?.getSelected()?.get(i)?.quantity?:0)
            }

        } else {
            addProductButton?.visibility = View.GONE
            Log.d("finalResult" , "NoSelection")
        }

    }

    override fun onIncreaseItemClick(position: Int, s: String) {

        quantityList[position-1] = s.toInt()
    }

    override fun onDecreaseItemClick(position: Int, text: String) {
        quantityList[position-1] = text.toInt()

    }
}