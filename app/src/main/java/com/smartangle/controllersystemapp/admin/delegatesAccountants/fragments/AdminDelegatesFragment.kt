package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters.AdminDelegatesAdapter
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_admin_delegates.*


class AdminDelegatesFragment : Fragment(), OnRecyclerItemClickListener {

    var webService: WebService? = null
    var progressDialog: Dialog? = null
    lateinit var modelHandleChangeFragmentclass: ViewModelHandleChangeFragmentclass

    lateinit var adminDelegatesAdapter: AdminDelegatesAdapter
    var delegatesList = ArrayList<Any>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        modelHandleChangeFragmentclass = UtilKotlin.declarViewModel(activity!!)!!

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_delegates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getData()

    }

    private fun getData() {

        delegatesList.clear()
        for (i in 0..4) {
            delegatesList.add("")
        }
        adminDelegateCount?.text = (delegatesList?.size?:0).toString()
        adminDelegatesAdapter = AdminDelegatesAdapter(context!!, delegatesList, this)
        adminDelegateRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
            adapter = adminDelegatesAdapter
        }
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt(NameUtils.WHICHID, R.id.frameLayout_direction)
        UtilKotlin.changeFragmentBack(activity!!, DelegateDetailsFragment(), "", bundle, R.id.frameLayout_direction)
    }
}