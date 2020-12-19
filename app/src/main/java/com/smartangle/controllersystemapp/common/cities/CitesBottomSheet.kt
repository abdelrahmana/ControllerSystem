package com.smartangle.controllersystemapp.common.cities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_cites_bottom_sheet.*
import okhttp3.ResponseBody

class CitesBottomSheet : BottomSheetDialogFragment() , CitiesPresenter.CommonCallsInterface{

    lateinit var progressDialog : Dialog
    var mServiceUser: WebService? = null
    lateinit var model : ViewModelHandleChangeFragmentclass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setContentView(R.layout.fragment_cites_bottom_sheet)

        dialog.setOnShowListener {
            val castDialog = it as BottomSheetDialog
            val bottomSheet = castDialog.findViewById<View?>(R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }

        return dialog
    }
    fun showLoader() { // dont call this until onactivity created
        //  loader!!.visibility = View.VISIBLE
        progressDialog.show()
        progressDialog.setCancelable(true)
        this.isCancelable =true

    }

    fun hideLoader() {
        try {
            this.isCancelable =true
            progressDialog.dismiss()
            //loader!!.visibility = View.GONE
        }catch (e : Exception) {

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_cites_bottom_sheet, container, false)
        mServiceUser  = ApiManagerDefault(context!!).apiService // set the default
        model = UtilKotlin.declarViewModel(activity!!)!!
        progressDialog = UtilKotlin.ProgressDialog(activity!!)

        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        rec?.setLayoutManager(linearLayoutManager)

        getCities()

        closeButton?.setOnClickListener{
            dismiss()
        }
        // dismiss dialog
        model.responseDataCode.observe(activity!!, Observer<Any> { updatedObject -> // when setting data

//            if (updatedObject !=null)
//            {
//                progressDialog?.hide()
//
//                if (updatedObject is Cities)
//                {
//                    Log.d("TAG" , "dissmis")
//                    dismiss() // when data setted dismiss here please
//                }
//
//                if (updatedObject is CitiesListResponse)
//                {
//                    Log.d("TAG" , "citiesData")
//
//                    setCitiesData(updatedObject)
//                    model.responseCodeDataSetter(null) // start details with this data please
//                }
//
//              //  model.responseCodeDataSetter(null) // start details with this data please
//
//            }

            if (updatedObject !=null)
            // lets dismiss this please
                dismiss() // when data setted dismiss here please
        })

//
//        model.errorMessage.observe(activity!! , Observer { error ->
//
//            if (error != null)
//            {
//                progressDialog?.hide()
//                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
//                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)
//
//                model.onError(null)
//            }
//
//        })


    }

    private fun setCitiesData(citiesListResponse: CitiesListResponse) {

        Log.d("TAG" , "insidecitiesData")
       hideLoader()
        val cities = ArrayList<Cities>()
        if (citiesListResponse.data?.list?.isNullOrEmpty() == false) {
            cities.clear()
            cities.addAll(citiesListResponse.data?.list)

            val cityAdaptor = CityAdapter(model, cities)
            miniviewHeight?.visibility = View.INVISIBLE
            rec?.adapter = cityAdaptor

        }



    }

    private fun getCities() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            showLoader()
            CitiesPresenter.getCitiesList(mServiceUser!!  , activity!! , this@CitesBottomSheet)

        } else {
            hideLoader()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

//    override fun onDestroyView() {
//        model.let {
//           // it?.errorMessage?.removeObservers(activity!!)
//            it?.responseDataCode?.removeObservers(activity!!)
//
//        }
//        super.onDestroyView()
//    }

    override fun setError(error: ResponseBody) {
        if (error != null)
        {
            progressDialog?.hide()
            val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
            UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

            model.onError(null)
        }
    }

    override fun getCitiesList(cityResponse: CitiesListResponse) {
        setCitiesData(cityResponse)
    }


}