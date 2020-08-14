package com.example.controllersystemapp.admin.settings.admin

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
import com.example.controllersystemapp.admin.specialcustomers.SpecialCustomerAdapter
import com.example.controllersystemapp.bottomsheets.AdminBottomSheet
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_admin.*
import kotlinx.android.synthetic.main.fragment_admin_specical_customersragment.*

class AdminFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var rootView: View
    var adminList = ArrayList<Any>()
    lateinit var adminAdapter: AdminAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageAdmin?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }


        addAdminBtn?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! ,
                AddAdminFragment(), "AddAdmin" , null)

        }


    }

    override fun onResume() {
        super.onResume()

        getAdminData()

    }

    private fun getAdminData() {

        adminList.clear()
        for (i in 0..6)
        {
            adminList.add("")
        }
        adminAdapter = AdminAdapter(adminList , this)
        adminRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = adminAdapter
        }

    }

    override fun onItemClick(position: Int) {

        Log.d("click" , "admin")
        val adminBottomSheet = AdminBottomSheet.newInstance()
        adminBottomSheet.show(activity?.supportFragmentManager!!, AdminBottomSheet.TAG)

    }
}