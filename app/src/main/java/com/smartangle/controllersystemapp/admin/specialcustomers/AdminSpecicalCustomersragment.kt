package com.smartangle.controllersystemapp.admin.specialcustomers

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_admin_specical_customersragment.*

class AdminSpecicalCustomersragment : Fragment() , OnRecyclerItemClickListener{

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog


    lateinit var specialCustomerAdapter: SpecialCustomerAdapter
    var customerList = ArrayList<ClientsData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_admin_specical_customersragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(specialCustomersRecycler)

        searchTextEdit?.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    getCustomersData()

                    return@OnEditorActionListener true
                }
            }
            return@OnEditorActionListener false
        })
        backImage?.setOnClickListener {

            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        addCustomerBtn?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! , AddCustomerFragment() , "AddCustomer"  , null,R.id.frameLayout_direction)
        }


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
//                removeClientItem(it)
//
//            }
//        }
//    }

//    private fun removeClientItem(position: Int) {
//
//        if (UtilKotlin.isNetworkAvailable(context!!)) {
//            progressDialog?.show()
//
//            ClientsPresenter.deleteClientPresenter(webService!! ,
//                customerList[position].id?:-1 , null , activity!! , model)
//
//        } else {
//            progressDialog?.dismiss()
//            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))
//
//        }
//
//    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ClientsListResponse) {
                    Log.d("testApi", "isForyou")
                    getClientsData(datamodel)
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
//            getCustomersData()
//
//        }
//
//
//
//
//
//    }

    private fun getClientsData(clientsListResponse: ClientsListResponse) {

        if (clientsListResponse?.data?.list?.isNullOrEmpty() == false)
        {
            customerList.clear()
            customerList.addAll(clientsListResponse?.data?.list)
            specialCustomerAdapter = SpecialCustomerAdapter(customerList , this)
            specialCustomersRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = specialCustomerAdapter
            }


        }
        else{
            //empty
        }

    }

    override fun onResume() {
        super.onResume()

        getCustomersData()

    }

    private fun getCustomersData() {

        val hashMap = HashMap<String,Any>()
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
             if (searchTextEdit?.text?.isNotEmpty()==true) // if there is search lets search
                 hashMap.put("name",searchTextEdit?.text.toString())
            ClientsPresenter.getClientsList(webService!! , activity!! , model,hashMap)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    override fun onItemClick(position: Int) {

        Log.d("click" , "customers")
        val bundle = Bundle()
        bundle.putInt(CLIENT_ID, customerList[position].id?:0)

        UtilKotlin.changeFragmentBack(activity!! ,
            ClientsDetailsFragment() , "ClientsDetailsFragment"  , bundle,R.id.frameLayout_direction)


    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }


    companion object{

        val CLIENT_ID = "clientId"

    }
}