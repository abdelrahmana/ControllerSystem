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
import com.smartangle.controllersystemapp.ModelStringID
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.AccountantPresenter
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantData
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantListResponse
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.admin.storesproducts.adapters.ResponsiblePersonAdapter
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_responsible_person.*


class ResponsiblePersonFragment : Fragment() , OnRecyclerItemClickListener {

    lateinit var model : ViewModelHandleChangeFragmentclass
    lateinit var rootView: View

    var webService: WebService? = null
    lateinit var progressDialog : Dialog


    lateinit var responsiblePersonAdapter: ResponsiblePersonAdapter
    var personList = ArrayList<AccountantData>()

    var personName = ""
    var personId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // model = UtilKotlin.declarViewModel(this)!!

        rootView = inflater.inflate(R.layout.fragment_responsible_person, container, false)

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

//        activity?.run {
//            model = ViewModelProviders.of(activity!!).get(ViewModelHandleChangeFragmentclass::class.java)
//        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        confirmResponsiblePersonBtn?.setOnClickListener {
            model.setStringData(ModelStringID(personName , personId))
            activity?.supportFragmentManager?.popBackStack()
        }

        Log.d("model" , "respo")

        observeData()

       // changeString()
    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AccountantListResponse) {
                    Log.d("testApi", "isForyou")
                    getPersonList(datamodel)
                }

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

    override fun onResume() {
        super.onResume()

        getData()
        //getPersonList()

    }

    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantPresenter.getAccountantsList(webService!! , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun getPersonList(accountantListResponse: AccountantListResponse) {

        if (accountantListResponse?.data?.list?.isNullOrEmpty() == false)
        {
            personList.clear()
            personList.addAll(accountantListResponse?.data?.list)
            responsiblePersonAdapter = ResponsiblePersonAdapter (personList , this)
            responsiblePersonRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = responsiblePersonAdapter
            }



        }
        else{

        }




    }

    override fun onItemClick(position: Int) {

        Log.d("clickRespon" , "$position")
        personName = personList[position].name!!
        personId = personList[position].id!!
        confirmResponsiblePersonBtn.visibility = View.VISIBLE

    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}