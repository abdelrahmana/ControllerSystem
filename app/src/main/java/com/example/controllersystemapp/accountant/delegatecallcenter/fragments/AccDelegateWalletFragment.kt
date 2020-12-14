package com.example.controllersystemapp.accountant.delegatecallcenter.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.EditDebtsSheet
import com.example.controllersystemapp.accountant.delegatecallcenter.EditDebtsSheet.Companion.DEBTS_QUANTITY
import com.example.controllersystemapp.accountant.delegatecallcenter.adapters.AccDelegateWalletAdapter
import com.example.controllersystemapp.accountant.delegatecallcenter.adapters.ViewPagerAccDelegateDetailsAdapter
import com.example.controllersystemapp.accountant.delegatecallcenter.debts.AccountantDebtsList
import com.example.controllersystemapp.accountant.delegatecallcenter.debts.AccountantDebtsListResponse
import com.example.controllersystemapp.accountant.delegatecallcenter.debts.AccountantDebtsPresenter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NestedScrollPaginationView
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_acc_delegate_wallet.*


class AccDelegateWalletFragment : Fragment(), OnRecyclerItemClickListener,
    NestedScrollPaginationView.OnMyScrollChangeListener {


    var walletList = ArrayList<AccountantDebtsList>()
    lateinit var accDelegateWalletAdapter: AccDelegateWalletAdapter

    var page = 1
    var hasMorePages = false
    var webService: WebService? = null
    var progressDialog: Dialog? = null
    lateinit var modelHandleChangeFragmentclass: ViewModelHandleChangeFragmentclass

    var delegateId = 0
    var selectedItemPosition = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        modelHandleChangeFragmentclass = UtilKotlin.declarViewModel(activity!!)!!
        delegateId = arguments?.getInt(ViewPagerAccDelegateDetailsAdapter.ACC_DELEGATE_ID, 0) ?: 0

        return inflater.inflate(R.layout.fragment_acc_delegate_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        scrollingNestedScroll?.myScrollChangeListener = this  // configure with this

        initRecyceler()
        observeData()

    }

    private fun observeData() {

        modelHandleChangeFragmentclass.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AccountantDebtsListResponse) {
                    Log.d("testApi", "isForyou")
                    getData(datamodel)
                }

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    getSuccessMessage(datamodel)
                }

                modelHandleChangeFragmentclass.responseCodeDataSetter(null) // start details with this data please
            }

        })


        modelHandleChangeFragmentclass.errorMessage.observe(activity!!, Observer { error ->

            if (error != null) {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                modelHandleChangeFragmentclass.onError(null)
            }

        })

        modelHandleChangeFragmentclass.notifyItemSelected?.observe(
            activity!!,
            Observer { datamodel ->

                if (datamodel != null) {

                    if (datamodel is String) {

                        Log.d("quantityData", "equal $datamodel")
                        updateDebt(datamodel)


                    }

                    if (datamodel == 2) { //delete debts

                        Log.d("quantityData", "equal $datamodel")
                        deleteDebts()


                    }

                    modelHandleChangeFragmentclass.setNotifyItemSelected(null) // start details with this data please
                }

            })

    }

    private fun deleteDebts() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AccountantDebtsPresenter.deleteDebts(
                webService!!,
                walletList[selectedItemPosition].id ?: 0
                , activity!!, modelHandleChangeFragmentclass
            )


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun getSuccessMessage(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false) {
            activity?.let {
                UtilKotlin.showSnackMessage(it, successModel?.msg[0])
            }

//            productsAdapter.let {
//                it?.removeItemFromList(removePosition)
//            }
//            productsAdapter?.notifyDataSetChanged()

            resetPaginationAndData()

//            model.responseCodeDataSetter(null) // start details with this data please
//            activity?.supportFragmentManager?.popBackStack()

        }
    }

    private fun resetPaginationAndData() {
        walletList.clear()
        page = 1
        scrollingNestedScroll?.resetPageCounter()
        getWalletData()
    }

    private fun updateDebt(quantity: String) {


        if (quantity.toInt() > ((walletList[selectedItemPosition].product?.total_quantity)
                ?: "").toInt()
        ) {
            UtilKotlin.showSnackErrorInto(activity!!, getString(R.string.max_quantity))
        } else {
            // not update if no change
            if (quantity.toInt() != ((walletList[selectedItemPosition].quantity)
                    ?: "").toInt()
            ) {
                if (UtilKotlin.isNetworkAvailable(context!!)) {
                    progressDialog?.show()
                    AccountantDebtsPresenter.accountantEditDebt(
                        webService!!,
                        walletList[selectedItemPosition].id ?: 0
                        , quantity?.toInt() ?: 0, activity!!, modelHandleChangeFragmentclass
                    )


                } else {
                    progressDialog?.dismiss()
                    UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

                }
            }
        }


    }

    private fun getData(accountantDebtsListResponse: AccountantDebtsListResponse) {

        hasMorePages = accountantDebtsListResponse.data?.has_more_page ?: false

        if (!accountantDebtsListResponse.data?.list.isNullOrEmpty()) {
            //collectionList.clear()
            accDelegateWalletRecycler?.visibility = View.VISIBLE
            noData?.visibility = View.GONE
            walletList.addAll(accountantDebtsListResponse.data?.list!!)
            accDelegateWalletAdapter.notifyDataSetChanged()


        } else {

            //empty
            if (page == 1) {
                accDelegateWalletRecycler?.visibility = View.GONE
                noData?.visibility = View.VISIBLE
            }


        }

    }

    private fun initRecyceler() {

        accDelegateWalletAdapter =
            AccDelegateWalletAdapter(
                context!!,
                walletList,
                this
            )

        accDelegateWalletRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
            adapter = accDelegateWalletAdapter

        }


    }

    override fun onResume() {
        super.onResume()

        resetPaginationAndData()


    }

    private fun getWalletData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AccountantDebtsPresenter.accountantDebtsList(
                webService!!,
                page,
                delegateId,
                activity!!,
                modelHandleChangeFragmentclass
            )


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }


    override fun onItemClick(position: Int) {

        selectedItemPosition = position
        val bundle = Bundle()
        bundle.putString(DEBTS_QUANTITY, walletList[position].quantity ?: "")
        val editDebtsSheet = EditDebtsSheet()
        editDebtsSheet.arguments = bundle
        editDebtsSheet.show(activity?.supportFragmentManager!!, "bottomSheetActions")
//        val bundle = Bundle()
//        bundle.putInt(ViewPagerAccDelegateDetailsAdapter.ACC_DELEGATE_ID, 15)
//        UtilKotlin.changeFragmentBack(activity!! ,
//            AccDelegateWalletFragment(), ""  ,
//            bundle , R.id.redirect_acc_fragments)

    }

    override fun onLoadMore(currentPage: Int) {
        Log.d("logLoad", "rrrrrr")

        if (hasMorePages) {
            Log.d("logLoad", "test")

            page = currentPage // set page to new page
            if (currentPage != 1)
                getWalletData()
        }
    }

    override fun onDestroyView() {

        modelHandleChangeFragmentclass.let {
            it?.responseDataCode?.removeObservers(activity!!)
            it?.errorMessage?.removeObservers(activity!!)
        }
        super.onDestroyView()
    }
}