package com.example.controllersystemapp.accountant.settings.expenses

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
import com.example.controllersystemapp.ModelStringID
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemWithLongClickListener
import com.example.controllersystemapp.bottomsheets.EditDeleteBottomSheet
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils.DELETE
import com.example.util.NameUtils.UPDATE
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_expenses.*


class ExpensesFragment : Fragment() , OnRecyclerItemWithLongClickListener{


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    lateinit var accountantExpensesAdapter: AccountantExpensesAdapter
    var expensesList = ArrayList<Data>()

     var listPosition: Int ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_expenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageFees?.setOnClickListener {

            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }


        addExpenseBtn?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! ,
                AddAccountantExpensesFragment(), "AddAccountantExpensesFragment" ,
                null,R.id.redirect_acc_fragments)

        }


        observeExpensesData()

    }

    private fun observeExpensesData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AccountantExpensesListResponse) {
                    Log.d("testApi", "isForyou")
                    setExpensesData(datamodel)
                }

               if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    setSuccessData(datamodel)
                }
                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.updateOrDelete?.observe(viewLifecycleOwner, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                setUpdateOrDelete(datamodel)


                model.setUpdateOrDelete(null) // start details with this data please
            }

        })


        model.errorMessage.observe(activity!! , Observer { error ->

            if (error != null)
            {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })


    }

    private fun setSuccessData(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {

            if (successModel?.msg?.isNullOrEmpty() == false)
            {
                UtilKotlin.showSnackMessage(activity , successModel?.msg[0])

                getExpensesData()

//                accountantExpensesAdapter.let {
//                it?.removeItemFromList(removePosition)
            }
//            productsAdapter?.notifyDataSetChanged()
//
//                getCateogriesData()
//
//            }

        }


    }

    private fun setUpdateOrDelete(optionSelect: String) {

        when(optionSelect)
        {
            DELETE -> {
                Log.d("test" , "delete $listPosition")
                listPosition?.let {
                    deleteExpenses(expensesList[it].id?:-1)

                }
            }

            UPDATE -> {
                Log.d("test" , "update $listPosition")

                val bundle = Bundle()
                listPosition?.let {
                    bundle.putInt(EXPENSES_ID , expensesList[it].id?:-1)
                    bundle.putString(EXPENSES_TITLE , expensesList[it].title?:"")
                    bundle.putString(EXPENSES_PRICE , expensesList[it].price?:"")
                    bundle.putString(EXPENSES_DESC , expensesList[it].details?:"")
                }
                UtilKotlin.changeFragmentBack(activity!! ,
                    AddAccountantExpensesFragment(), "AddAccountantExpensesFragment" ,
                    bundle , R.id.redirect_acc_fragments)

            }
        }


    }

    private fun deleteExpenses(id: Int) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ExpensesPresenter.deleteExpensesData(webService!! ,id , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    private fun setExpensesData(accountantExpensesListResponse: AccountantExpensesListResponse) {

        if (accountantExpensesListResponse?.data?.isNullOrEmpty() == false)
        {
            expenseRecycler?.visibility = View.VISIBLE
            noProductData?.visibility = View.GONE

            expensesList.clear()
            expensesList.addAll(accountantExpensesListResponse?.data)
            accountantExpensesAdapter = AccountantExpensesAdapter(context!! , expensesList , this)
            expenseRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = accountantExpensesAdapter
            }

        }else{

            //empty
            expenseRecycler?.visibility = View.GONE
            noProductData?.visibility = View.VISIBLE

        }





    }

    override fun onResume() {
        super.onResume()


        getExpensesData()

    }

    private fun getExpensesData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ExpensesPresenter.expensesListData(webService!! , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    override fun onItemClick(position: Int) {
        Log.d("test" , "click")
    }

    override fun onItemLongClick(position: Int) {
        Log.d("test" , "long click")
        listPosition = position
        val addPhotoBottomDialogFragment: EditDeleteBottomSheet =
            EditDeleteBottomSheet.newInstance()
//        val bundle = Bundle()
//        bundle.putInt(EXPENSES_ID , position)
//        addPhotoBottomDialogFragment.arguments = bundle
        addPhotoBottomDialogFragment.show(
            activity?.supportFragmentManager!!,
            EditDeleteBottomSheet.TAG
        )
    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)
            it?.updateOrDelete?.removeObservers(viewLifecycleOwner!!)

        }
        super.onDestroyView()
    }


    companion object
    {
        val EXPENSES_ID = "expensesId"
        val EXPENSES_TITLE = "expensesTitle"
        val EXPENSES_PRICE = "expensesPrice"
        val EXPENSES_DESC = "expensesDesc"


    }

}