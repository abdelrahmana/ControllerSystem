package com.example.controllersystemapp.admin.reports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.productclassification.lastsubcategory.FragmentLastSubProductclassification
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.ProductClassificationAdaptor
import com.photonect.photographerapp.notificationphotographer.DonePackgae.SalesItemAdapter
import kotlinx.android.synthetic.main.fragment_product_classification.*
import kotlinx.android.synthetic.main.fragment_sales.*


class SalesFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_sales, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleViewData()// when data is coming
        setViewModelListener()
    }
    var salesItemAdapter : SalesItemAdapter?=null
    private fun setRecycleViewData() {
        val arrayList = ArrayList<Any>()
        arrayList.add("")
        arrayList.add("")
        arrayList.add("")
        salesItemAdapter = SalesItemAdapter(model,arrayList)
        UtilKotlin.setRecycleView(salesRecycleView,salesItemAdapter!!,
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
                    UtilKotlin.changeFragmentBack(activity!! , ReportsDetailsFragment() , "ReportsDetailsFragment"  , null)

                }

                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null

            }
        })
    }
    companion object {
    }
}