package com.example.controllersystemapp.accountant.noticesandreports

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
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantData
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.example.controllersystemapp.admin.productclassification.productsubclassification.FragmentSubProductclassification
import com.example.controllersystemapp.admin.storesproducts.adapters.ResponsiblePersonAdapter
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_select_delegates.*


class SelectDelegatesFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var model : ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog : Dialog


    lateinit var selectDeleagteAdapter: SelectDeleagteAdapter
    var selectedDeleagtesList = ArrayList<AccountantData>()

    var personName = ""
    var personId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_select_delegates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()


        confirmSelectDelegateBtn?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }
    }

    private fun observeData() {


//        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
//            if (modelSelected != null) { // if null here so it's new service with no any data
//                Log.d("paretnId" , "observeParent")
//
//                if (modelSelected is ArrayList<*>) {
//
//
//                    Log.d("selectedData" , "size ${modelSelected.size}")
//
//                    for (i in modelSelected?.indices) {
//                        Log.d("selectedData" , "data ${modelSelected[i]}")
//
//                        //msgtext = msgtext + successModel?.msg?.get(i) + "\n"
//
//                    }
//
//
//
//                }
//                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
//
//            }
//
//            Log.d("paretnId" , "j22j")
//        })




    }

    override fun onResume() {
        super.onResume()

        getData()
    }

    private fun getData() {


        selectedDeleagtesList.clear()
        for (i in 0 .. 5)
        {
            selectedDeleagtesList.add(AccountantData(null , null , null ,
                i+1 , null , null ,
                null , "احمد حازم" , "+966 56784 9876" , null , null))
        }

        selectDeleagteAdapter = SelectDeleagteAdapter(selectedDeleagtesList , this , model)
        selectDelegateRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = selectDeleagteAdapter


        }

    }

    override fun onItemClick(position: Int) {
        Log.d("clickRespon" , "$position")

    }
}