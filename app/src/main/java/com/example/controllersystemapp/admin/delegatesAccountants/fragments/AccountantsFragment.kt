package com.example.controllersystemapp.admin.delegatesAccountants.fragments

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
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantModel
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_accountant.*

class AccountantsFragment : Fragment() , OnRecyclerItemClickListener{


    lateinit var accountantAdapter: AccountantAdapter
    var accountantsList = ArrayList<AccountantModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accountant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAccountantData()
    }

    private fun getAccountantData() {

        accountantsList.clear()
        for (i in 0..4)
        {
            accountantsList.add(AccountantModel("عمرو صالح" , null , "+966 56784 9876" , i+1))
        }

        accountantCount?.text = accountantsList.size.toString()
        accountantAdapter = AccountantAdapter(context!! , accountantsList , this)
        accountantRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = accountantAdapter
        }


    }

    override fun onItemClick(position: Int) {
        Log.d("clickAccount" , "${accountantsList[position].Id}")

    }
}