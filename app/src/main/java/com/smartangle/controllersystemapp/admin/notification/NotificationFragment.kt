package com.smartangle.controllersystemapp.admin.notification

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
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.CommonPresenter.getNotificationList
import com.smartangle.util.UtilKotlin
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.no_products.*
import retrofit2.Response


class NotificationFragment : Fragment() , OnRecyclerItemClickListener {


    lateinit var notificationAdapter: NotificationAdapter
    var notificationList = ArrayList<Data>()
    var progressDialog : Dialog? =null
    var webService : WebService? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        webService = ApiManagerDefault(context!!).apiService
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleNoDataViews()

        backImageAdmin?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }
        getNotificationApi()
    }

    private fun handleNoDataViews() {

        no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!! , R.drawable.ic_img_notification))
        firstNoDataTxt?.text = getString(R.string.no_notifications)
        secondNoDataTxt?.visibility = View.GONE


    }
    fun getNotificationApi() {
        if(UtilKotlin.isNetworkAvailable(context!!))
        {
            progressDialog?.show()
            getNotificationList(
                webService!!,
                dispossibleObserver()
            )
        }
        else{
            UtilKotlin.showSnackMessage(activity!!,getString(R.string.no_connect))
        }
    }

    var disposableObserver : DisposableObserver<Response<NotificationResponse>>?=null
    private fun dispossibleObserver(): DisposableObserver<Response<NotificationResponse>> {

        disposableObserver= object : DisposableObserver<Response<NotificationResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<NotificationResponse>) {
                progressDialog?.dismiss()
                if (response!!.isSuccessful) {

                    getNotificationData(response?.body()?.data)// when data is coming

                }
                else
                {
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
        return disposableObserver!!
    }
    override fun onResume() {
        super.onResume()

     //   getNotificationData()
    }

    override fun onDestroyView() {
        disposableObserver?.dispose()
        super.onDestroyView()
    }

    private fun getNotificationData(data: ArrayList<Data>?) {

        notificationRecycler?.visibility = View.VISIBLE
        notificationList.clear()
        notificationList.addAll(data?:ArrayList())

        notificationAdapter = NotificationAdapter(notificationList , this)
        notificationRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = notificationAdapter
        }


    }

    override fun onItemClick(position: Int) {
   //     Log.d("click" , "notification")
    }
}