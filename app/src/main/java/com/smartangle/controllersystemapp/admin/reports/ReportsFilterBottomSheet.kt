package com.smartangle.controllersystemapp.admin.reports

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartangle.controllersystemapp.R
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import kotlinx.coroutines.Job

// used to get area and city besides get the medical
class ReportsFilterBottomSheet : BottomSheetDialogFragment()/*, CommonPresenter.CommonCallsInterface*/{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
 override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setContentView(R.layout.filterbottom_sheet)

        dialog.setOnShowListener {
            val castDialog = it as BottomSheetDialog
            val bottomSheet = castDialog.findViewById<View?>(R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_DRAGGING
                    }
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
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


    var mServiceUser: WebService? = null
   lateinit var model : ViewModelHandleChangeFragmentclass
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.filterbottom_sheet, container, false)

        model = UtilKotlin.declarViewModel(activity!!)!!
            // t his is used to update sign up fragment with user data
            //ViewModelProviders.of(this)[ViewModelData::class.java]

        return v
    }
      lateinit var progressDialog : Dialog
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
     var selectedObjectResult = ""
  /*  private fun getMedicalCompaniesList() {
        if (MyUtils.isNetworkAvailable(context!!)) {
           showLoader()
            networkCall = GlobalScope.launch(Dispatchers.Main) {

                CommonPresenter.getCompaniesMedical(this@CountryCurrencySheet, mServiceUser!!, context!!)

            }
        } else {
            hideLoader()
            MyUtils.showSnackErrorInto(activity, getString(R.string.no_connect))
        }
    }*/

    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
             override fun onGlobalLayout() {

                 view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                 val dialog = dialog as BottomSheetDialog
                 val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                 val behavior = BottomSheetBehavior.from(bottomSheet!!)
              //   behavior.state = BottomSheetBehavior.PEEK_HEIGHT_AUTO

                 val newHeight = activity?.window?.decorView?.measuredHeight
                 val viewGroupLayoutParams = bottomSheet.layoutParams
                 viewGroupLayoutParams.height = newHeight ?: 0
               bottomSheet.layoutParams = viewGroupLayoutParams
                 BottomSheetBehavior.from(bottomSheet!!).peekHeight = 700
             }
         })
        // dialogView = view
     }*/


  /*  private fun getCities() { // get cities
        if (MyUtils.isNetworkAvailable(context!!)) {
            showLoader()
            networkCall = GlobalScope.launch(Dispatchers.Main) {

                CommonPresenter.getCities(this@CountryCurrencySheet, mServiceUser!!,context!!)

            }
            /*mServie.getCities(MyUtil.getLocalLanguage(getActivity()), country_id).enqueue(new Callback<AreasResponse>() {
                @Override
                public void onResponse(Call<AreasResponse> call, Response<AreasResponse> response) {
                    if (response.body().getData() != null && !response.body().getData().isEmpty()) {
                        cities.clear();
                        cities.addAll(response.body().getData());
                        hideLoading();
                    }

                    citySpinner.setAdapter(new HintSpinnerAdapter<AreasResponse.DataBean>(getContext(), cities, getString(R.string.choose_city)) {
                        @Override
                        public String getLabelFor(AreasResponse.DataBean object) {
                            return object.getCity_name();
                        }
                    });
                }

                @Override
                public void onFailure(Call<AreasResponse> call, Throwable t) {
                    hideLoading();
                    MyUtil.showSnackErrorInto(getActivity(), getString(R.string.error), t.getMessage());
                }
            });*/
        } else {
            hideLoader()
            MyUtils.showSnackErrorInto(activity, getString(R.string.no_connect))
        }
    }*/
     var networkCall : Job? = null
   /* private fun getAreasApi(cityId: Int) {
        if (MyUtils.isNetworkAvailable(activity!!)) {
            showLoader()
            networkCall =  GlobalScope.launch(Dispatchers.Main) {
                // now run this on ui thread
                CommonPresenter.getArea(cityId, this@CountryCurrencySheet, mServiceUser!!,context!!)

            }
            /*  mServie.getCountries(MyUtil.getLocalLanguage(getActivity())).enqueue(new Callback<CountriesResponse>() {
                @Override
                public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getData() != null && !response.body().getData().isEmpty()) {
                            countries.clear();
                            countries.addAll(response.body().getData());
                            hideLoading();
                            signupPhone.setText("");
                        }

                        countrySpinner.setAdapter(new HintSpinnerAdapter<CountriesResponse.DataBean>(getContext(), countries, getString(R.string.choose_a_country)) {
                            @Override
                            public String getLabelFor(CountriesResponse.DataBean object) {
                                return object.getCountry_name();
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<CountriesResponse> call, Throwable t) {
                    hideLoading();
                    MyUtil.showSnackErrorInto(getActivity(), getString(R.string.error), t.getMessage());
                }
            });*/
        } else {
            hideLoader()
            MyUtils.showSnackErrorInto(activity, getString(R.string.no_connect))
        }
    }*/
    companion object {
        val  thisIsCountriesList = "countries_list"
       // val  regionArea = "regionArea"
        val  currencyList = "curecny_list"

        val whichOne = "which_sheet"
    //    val cityId = "city_id"
    }
 // when error happend
  /*  override fun setError(error: String) {
     Handler(Looper.getMainLooper()).post {
         hideLoader()
         MyUtils.showSnackErrorInto(activity!!, error)
     }
 }*/

    override fun onDestroyView() {
        networkCall.let{ // if there is a call remove it
            it?.cancel()
        }
        super.onDestroyView()
    }

}