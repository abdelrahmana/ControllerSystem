package com.smartangle.controllersystemapp.common.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.common.delivery.deliverydetails.DeliveryDetailsFragment
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.DeliveryItemAdapter
import kotlinx.android.synthetic.main.fragment_admin_delivery.*


class DeliveryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = UtilKotlin.declarViewModel(activity!!)!!
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleViewData()
        setViewModelListener()
    }
    lateinit var model : ViewModelHandleChangeFragmentclass

    var deliveryItemAdapter : DeliveryItemAdapter?=null
    private fun setRecycleViewData() {
        val arrayList = ArrayList<Any>()
        arrayList.add("")
        arrayList.add("")
        arrayList.add("")
        deliveryItemAdapter = DeliveryItemAdapter(model,arrayList)
        UtilKotlin.setRecycleView(deliveryRecycleView,deliveryItemAdapter!!,
            RecyclerView.VERTICAL,context!!, null, true)
    }


    override fun onDestroyView() {
        model.notifyItemSelected.removeObservers(activity!!)
        super.onDestroyView()
    }

    fun setViewModelListener() {
        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Any) {
                    // if (modelSelected.isItCurrent) {
                    // initSlider(modelSelected.pictures)
                    // }
                    val bundle = Bundle()
                    //     bundle.putInt(EXITENCEIDPACKAGE,availableServiceList.get(position).id?:-1)
                    //  UtilKotlin.changeFragmentWithBack(activity!! , R.id.container , ReportsDetailsFragment() , bundle)
                    UtilKotlin.changeFragmentBack(activity!! , DeliveryDetailsFragment() , "ReportsDetailsFragment"  ,
                        null,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)

                }

                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null

            }
        })
    }
}