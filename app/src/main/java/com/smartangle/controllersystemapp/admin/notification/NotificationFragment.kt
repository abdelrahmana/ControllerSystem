package com.smartangle.controllersystemapp.admin.notification

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
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.no_products.*


class NotificationFragment : Fragment() , OnRecyclerItemClickListener {


    lateinit var notificationAdapter: NotificationAdapter
    var notificationList = ArrayList<Any>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }

    private fun handleNoDataViews() {

        no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!! , R.drawable.ic_img_notification))
        firstNoDataTxt?.text = getString(R.string.no_notifications)
        secondNoDataTxt?.visibility = View.GONE


    }

    override fun onResume() {
        super.onResume()

        getNotificationData()
    }

    private fun getNotificationData() {

        notificationRecycler?.visibility = View.VISIBLE
        noDataLayout?.visibility = View.GONE

        notificationList.clear()
        for (i in 0..3)
        {
            notificationList.add("")
        }

        notificationAdapter = NotificationAdapter(notificationList , this)
        notificationRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = notificationAdapter
        }


    }

    override fun onItemClick(position: Int) {
        Log.d("click" , "notification")
    }
}