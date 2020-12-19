package com.smartangle.controllersystemapp.admin.storesproducts.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.admin.storesproducts.StoresPresenter
import com.smartangle.controllersystemapp.admin.storesproducts.adapters.StoresAdapter
import com.smartangle.controllersystemapp.admin.storesproducts.models.StoresData
import com.smartangle.controllersystemapp.admin.storesproducts.models.StoresListResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_stores.*


class StoresFragment : Fragment() , OnRecyclerItemClickListener {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog



    lateinit var rootView: View
    var storeList = ArrayList<StoresData>()
    lateinit var storesAdapter: StoresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_stores, container, false)

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(storesRecycler)


        observeData()
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
//                removeStoreItem(it)
//
//            }
//        }
//    }

//    private fun removeStoreItem(position: Int) {
//
//        if (UtilKotlin.isNetworkAvailable(context!!)) {
//            progressDialog?.show()
//
//            StoresPresenter.deleteStorePresenter(webService!! ,
//                storeList[position].id?:-1 ,  activity!! , model)
//
//        } else {
//            progressDialog?.dismiss()
//            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))
//
//        }
//
//
//
//
//    }


    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is StoresListResponse) {
                    Log.d("testApi", "isForyou")
                    getStoresData(datamodel)
                }

//                if (datamodel is SuccessModel) {
//                    Log.d("testApi", "isForyou")
//                    successRemove(datamodel)
//                }
                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.errorMessage.observe(activity!! , Observer { error ->

            if (error != null)
            {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })




    }

//    private fun successRemove(successModel: SuccessModel) {
//
//        if (successModel?.msg?.isNullOrEmpty() == false)
//        {
//            activity?.let {
//                UtilKotlin.showSnackMessage(it,successModel?.msg[0])
//            }
//
////            productsAdapter.let {
////                it?.removeItemFromList(removePosition)
////            }
////            productsAdapter?.notifyDataSetChanged()
//
//            requestStoreData()
//
//        }
//
//
//
//    }

    override fun onResume() {
        super.onResume()

        //getStoresData()

        requestStoreData()

    }

    private fun requestStoreData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            StoresPresenter.getStoresList(webService!! , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun getStoresData(storesListResponse: StoresListResponse) {



        if(storesListResponse?.data?.isNullOrEmpty() == false)
        {
            storesRecycler?.visibility = View.VISIBLE
            noStoresData.visibility = View.GONE
            storeList.clear()
            storeList.addAll(storesListResponse?.data)
            storesAdapter = StoresAdapter(context!! , storeList , this)

            storesRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = storesAdapter

            }


        }
        else{
            storesRecycler?.visibility = View.GONE
            noStoresData.visibility = View.VISIBLE
        }



    }

    override fun onItemClick(position: Int) {
        Log.d("click" , "position $position name ${storeList[position].name}")

        val bundle = Bundle()
        bundle.putInt(STOREID, storeList[position].id?:0)
        UtilKotlin.changeFragmentBack(activity!! ,
            StoreDetailsFragment(), "StoreDetailsFragment"  ,
            bundle , R.id.frameLayout_direction)
    }


    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }


    companion object{

        val STOREID = "store_id"

    }

}