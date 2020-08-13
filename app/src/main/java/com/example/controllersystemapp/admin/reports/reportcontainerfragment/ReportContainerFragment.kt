package com.example.controllersystemapp.admin.reports.reportcontainerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.ProductClassificationAdaptor
import com.photonect.photographerapp.notificationphotographer.DonePackgae.SalesAdapterContainer
import com.photonect.photographerapp.notificationphotographer.DonePackgae.SalesItemAdapter
import kotlinx.android.synthetic.main.fragment_product_classification.*
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
        setRecycleViewData()// when data is coming
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
    companion object {
    }
}