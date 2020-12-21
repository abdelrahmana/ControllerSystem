package com.smartangle.controllersystemapp.admin.settings.masrufat

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
import com.smartangle.controllersystemapp.admin.settings.admin.AdminFragment
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_masrufat.*

class MasrufatFragment : Fragment() , ClickAcceptRejectListener {

    lateinit var masrufatAdapter: MasrufatAdapter
    var arrayList = ArrayList<Data>()

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_masrufat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageFees?.setOnClickListener {
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

    override fun onResume() {
        super.onResume()

        getDataList()

    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ExpensesListResponse) {
                    Log.d("testApi", "isForyou")
                    getAdminExpensesListData(datamodel)
                }

//                if (datamodel is SuccessModel) {
//                    Log.d("testApi", "isForyou")
//                    successRemove(datamodel)
//                }
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

    private fun getAdminExpensesListData(expensesListResponse: ExpensesListResponse) {

        if (expensesListResponse?.data?.isNullOrEmpty() == false)
        {
            masrufatRecycler?.visibility = View.VISIBLE
            arrayList.clear()
            arrayList.addAll(expensesListResponse?.data)
            masrufatAdapter = MasrufatAdapter(arrayList , this)
            masrufatRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = masrufatAdapter
            }

        }
        else{
            masrufatRecycler?.visibility = View.GONE
            noProductData?.visibility = View.VISIBLE

        }

    }

    private fun getDataList() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AdminExpensesPresenter.getAdminExpensesList(webService!!, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }


    override fun onItemListClick(position: Int) {

        val bundle = Bundle()
        bundle.putInt(ADMIN_EXPENSES_ID, arrayList[position].id?:0)

        UtilKotlin.changeFragmentBack(activity!! ,
            FeesDetailsFragment(), "FeesDetails" , null,R.id.frameLayout_direction)

    }

    override fun onAcceptClick(position: Int) {
        Log.d("click" , "accept")
    }

    override fun onRejectClick(position: Int) {
        Log.d("click" , "reject")
    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    companion object{

        val ADMIN_EXPENSES_ID = "adminExpensesId"

    }
}