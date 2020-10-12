package com.example.controllersystemapp.accountant.products

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_accountant_products.*


class AccountantProductsFragment : Fragment(), OnRecyclerItemClickListener {


    var productsList = ArrayList<Any>()
    lateinit var accountantProductsAdapter: AccountantProductsAdapter

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

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

        return inflater.inflate(R.layout.fragment_accountant_products, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backIcon?.setOnClickListener {

            activity?.let {
                if (it.supportFragmentManager.backStackEntryCount == 1)
                {
                    it.finish()
                }
                else{
                    it.supportFragmentManager.popBackStack()
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()

        getData()
    }

    private fun getData() {

        productsList.clear()
        for (i in 0..5)
        {
            productsList.add("")
        }
        accountantProductsAdapter = AccountantProductsAdapter(context!! , productsList , this)
        accProdRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = accountantProductsAdapter
        }

    }

    override fun onItemClick(position: Int) {

        UtilKotlin.changeFragmentBack(activity!! , AccProdDetailsFragment() , ""  ,
            null,R.id.redirect_acc_fragments)
    }
}