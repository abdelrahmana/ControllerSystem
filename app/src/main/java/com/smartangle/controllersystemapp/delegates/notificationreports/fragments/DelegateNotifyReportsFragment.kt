package com.smartangle.controllersystemapp.delegates.notificationreports.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerIClickAcceptRejectListener
import com.smartangle.controllersystemapp.delegates.notificationreports.models.DelegateNotificationresponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.DelegateNotificationReportsAdapter
import kotlinx.android.synthetic.main.fragment_delegate_notify_reports.*
import kotlinx.android.synthetic.main.no_products.view.*

class DelegateNotifyReportsFragment : Fragment() , OnRecyclerIClickAcceptRejectListener {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var notificationList = ArrayList<DelegateNotificationresponse>()
    lateinit var delegaetNotificationReportsAdapter: DelegateNotificationReportsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delegate_notify_reports, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backNotificationReports?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        handleNoData()

    }

    private fun handleNoData() {
        noNotificationReportsData?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_reports_icon))
        noNotificationReportsData?.firstNoDataTxt?.text = getString(R.string.no_notifications_reports)
        noNotificationReportsData?.secondNoDataTxt?.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()

        requestData()

    }

    private fun requestData() {

        notificationReportsRecycler?.visibility = View.VISIBLE
        noNotificationReportsData?.visibility = View.GONE
        notificationList.clear()
        notificationList.add(DelegateNotificationresponse(null , false))
        notificationList.add(DelegateNotificationresponse(null , false))
        notificationList.add(DelegateNotificationresponse(null , false))

        delegaetNotificationReportsAdapter = DelegateNotificationReportsAdapter(model ,notificationList , this )
        notificationReportsRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = delegaetNotificationReportsAdapter

        }

    }

    override fun onItemAcceptClick(position: Int) {
        Log.d("clickButton" , "Accept")
    }

    override fun onItemRejectClick(position: Int) {
        Log.d("clickButton" , "reject")
    }


}