package com.smartangle.controllersystemapp.admin.reports.reportcontainerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.reports.SalesFragment
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.SalesAdapterContainer
import kotlinx.android.synthetic.main.fragment_reports_container.*


class ReportContainerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var model :ViewModelHandleChangeFragmentclass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = UtilKotlin.declarViewModel(activity!!)!!
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButtonReport?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }


        setRecycleViewData()// when data is coming
        setViewModelListener()
    }
    var salesItemAdapter : SalesAdapterContainer?=null
    private fun setRecycleViewData() {
        val arrayList = ArrayList<Any>()
        arrayList.add("")
        arrayList.add("")
        arrayList.add("")
        salesItemAdapter = SalesAdapterContainer(model,arrayList)
        UtilKotlin.setRecycleView(reportsRecycleView,salesItemAdapter!!,
            RecyclerView.VERTICAL,context!!, null, true)
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
                    //UtilKotlin.changeFragmentWithBack(activity!! , R.id.container , SalesFragment() , bundle)
                    UtilKotlin.changeFragmentBack(activity!! , SalesFragment() , "SalesFragment"  , null,R.id.frameLayout_direction)

                }

                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null

            }
        })
    }

    override fun onDestroyView() {
        model.notifyItemSelected.removeObservers(activity!!)
        super.onDestroyView()
    }

    companion object {
    }
}