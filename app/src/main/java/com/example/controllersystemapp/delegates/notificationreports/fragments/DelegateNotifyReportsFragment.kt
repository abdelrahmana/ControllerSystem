package com.example.controllersystemapp.delegates.notificationreports.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.controllersystemapp.R
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_delegate_notify_reports.*
import kotlinx.android.synthetic.main.no_products.view.*

class DelegateNotifyReportsFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog


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

        notificationReportsRecycler?.visibility = View.GONE
        noNotificationReportsData?.visibility = View.VISIBLE
    }




}