package com.example.controllersystemapp.admin.specialcustomers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.AccountantAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.DoneDialogFragment
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.util.UtilKotlin
import com.example.util.UtilKotlin.changeFragment
import kotlinx.android.synthetic.main.fragment_accountant.*
import kotlinx.android.synthetic.main.fragment_admin_specical_customersragment.*

class AdminSpecicalCustomersragment : Fragment() , OnRecyclerItemClickListener{

    lateinit var specialCustomerAdapter: SpecialCustomerAdapter
    var customerList = ArrayList<Any>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_specical_customersragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImage?.setOnClickListener {

            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        addCustomerBtn?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! , AddCustomerFragment() , "AddCustomer" )
        }
    }

    override fun onResume() {
        super.onResume()

        getCustomersData()

    }

    private fun getCustomersData() {

        customerList.clear()

        for (i in 0..6)
        {
            customerList.add("")
        }

        specialCustomerAdapter = SpecialCustomerAdapter(customerList , this)
        specialCustomersRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = specialCustomerAdapter
        }


    }

    override fun onItemClick(position: Int) {

        Log.d("click" , "customers")

    }
}