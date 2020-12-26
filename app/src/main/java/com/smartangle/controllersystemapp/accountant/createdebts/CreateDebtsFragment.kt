package com.smartangle.controllersystemapp.accountant.createdebts

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.debts.AccountantDebtsPresenter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.accountant.makeorder.fragments.AccDelegatesFragment
import com.smartangle.controllersystemapp.accountant.products.models.Data
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_create_debts.*


class CreateDebtsFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog

    var productName: String? = null
    var productQuantity: Int? = null
    var delegateName: String? = null

    var delegateId: Int? = null
    var productId: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)



        return inflater.inflate(R.layout.fragment_create_debts, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backOrderImg?.setOnClickListener {

            activity?.let {
                if (it.supportFragmentManager.backStackEntryCount == 1) {
                    it.finish()
                } else {
                    it.supportFragmentManager.popBackStack()
                }
            }

        }


        chooseDelegateCard?.setOnClickListener {

            UtilKotlin.changeFragmentBack(
                activity!!, AccDelegatesFragment(), "",
                null, R.id.redirect_acc_fragments
            )

        }

        chooseProductCard?.setOnClickListener {

//            UtilKotlin.changeFragmentBack(
//                activity!!, DebtsChooseProductFragment(), "",
//                null , R.id.redirect_acc_fragments
//            )

            val bundle = Bundle()
//            bundle.putInt(AccountantCategoriesFragment.ACC_CATEGORY_LAST_PARENT_ID, 0)
//            bundle.putString(AccountantCategoriesFragment.ACC_CATEGORY_LAST_PARENT_NAME, getString(R.string.product_level))
            UtilKotlin.changeFragmentWithBack(
                activity!!,
                R.id.redirect_acc_fragments,
                DebtsChooseProductFragment(), null
            )

        }

        addDebts?.setOnClickListener {

            if (checkDataValidation()) {

                postCreateDebts()
            }

        }


        observeData()


    }

    private fun postCreateDebts() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AccountantDebtsPresenter.accountantCreateDebts(
                webService!!,
                delegateId ?: 0, productId ?: 0
                , productQuantity ?: 1, activity!!, model
            )


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun checkDataValidation(): Boolean {

        var errorMessage = ""

        if (delegateId == null) {
            errorMessage += getString(R.string.delegate_required)
            errorMessage += "\n"
        }

        if (productId == null) {
            errorMessage += getString(R.string.product_classify_required)
            errorMessage += "\n"
        }

        return if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
            false

        } else {
            true
        }


    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testObserve", "inside")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testObserve", "notNull")

                if (datamodel is CallCenterDelegateData) {
                    Log.d("testObserve", "delegate ${datamodel?.id ?: 0}")
                    delegateId = datamodel?.id ?: 0
                    delegateName = datamodel?.name ?: ""
                    chooseDelegateTxt?.text = delegateName
                }

                if (datamodel is Data) {
                    Log.d("testObserve", "data")

                    getSelectedProducts(datamodel)
                }
                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    getSuccessMessage(datamodel)
                }


                model.responseCodeDataSetter(null) // start details with this data please
            } else {
                Log.d("testObserve", "nulll")

            }

        })


        model.errorMessage.observe(activity!!, Observer { error ->

            if (error != null) {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })


    }

    private fun getSuccessMessage(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false) {
            activity?.let {
                UtilKotlin.showSnackMessage(it, successModel?.msg[0])
            }

            model.responseCodeDataSetter(null) // start details with this data please

            Handler().postDelayed(Runnable {
                activity?.let {
                    if (it.supportFragmentManager.backStackEntryCount == 1) {
                        it.finish()
                    } else {
                        it.supportFragmentManager.popBackStack()
                    }
                }
            }, 1000)

            


        }

    }

    private fun getSelectedProducts(data: Data) {

        productId = data?.id ?: 0
        productName = data?.name ?: ""
        productQuantity = data?.selectedQuantity ?: 1
        chooseProductTxt?.text = productName

    }


    override fun onResume() {
        super.onResume()

        chooseDelegateTxt?.text = delegateName
        chooseProductTxt?.text = productName
    }

    override fun onDestroyView() {
        model.let {
            Log.d("destroyCrete", "true")

            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)
            // it?.notifyItemSelected?.removeObservers(activity!!)


        }
        super.onDestroyView()
    }
}