package com.smartangle.controllersystemapp.admin.makeorders

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
import com.smartangle.controllersystemapp.ModelStringID
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientsData
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientsListResponse
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientsPresenter
import com.smartangle.controllersystemapp.admin.specialcustomers.SpecialCustomerAdapter
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_customers_specify.*

class CustomersSpecifyFragment : Fragment() , OnRecyclerItemClickListener {

    lateinit var model : ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog : Dialog
    lateinit var rootView: View

    var personName = ""
    var personId = 0

    lateinit var customerClassifyAdapter: CustomerClassifyAdapter
    var customerList = ArrayList<ClientsData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        rootView = inflater.inflate(R.layout.fragment_customers_specify, container, false)



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEdit?.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    getCustomersData()

                    return@OnEditorActionListener true
                }
            }
            return@OnEditorActionListener false
        })
        close?.setOnClickListener {

            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        confirmResponsibleClientBtn?.setOnClickListener {
            model.setStringData(ModelStringID(personName , personId))
            activity?.supportFragmentManager?.popBackStack()
        }

        observeData()

    }

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


    private fun getClientsData(clientsListResponse: ClientsListResponse) {

        if (clientsListResponse?.data?.list?.isNullOrEmpty() == false)
        {
            customerList.clear()
            customerList.addAll(clientsListResponse?.data?.list)
            customerClassifyAdapter = CustomerClassifyAdapter(customerList , this)
            customerCalssifyRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = customerClassifyAdapter
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
            if (searchEdit?.text?.isNotEmpty()==true) // if there is search lets search
                hashMap.put("name",searchEdit?.text.toString())
            ClientsPresenter.getClientsList(webService!! , activity!! , model,hashMap)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }


    override fun onItemClick(position: Int) {

        Log.d("clickRespon" , "$position")
        personName = customerList[position].name!!
        personId = customerList[position].id!!
        confirmResponsibleClientBtn?.visibility = View.VISIBLE

    }


    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

}