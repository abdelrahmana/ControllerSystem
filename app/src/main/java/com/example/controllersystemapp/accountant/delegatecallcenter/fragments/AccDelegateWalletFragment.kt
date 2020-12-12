package com.example.controllersystemapp.accountant.delegatecallcenter.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.EditDebtsSheet
import com.example.controllersystemapp.accountant.delegatecallcenter.adapters.AccDelegateWalletAdapter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_acc_delegate_wallet.*


class AccDelegateWalletFragment : Fragment() , OnRecyclerItemClickListener {


    var walletList = ArrayList<Any>()
    lateinit var accDelegateWalletAdapter: AccDelegateWalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acc_delegate_wallet, container, false)
    }

    override fun onResume() {
        super.onResume()

        getWalletData()

    }

    private fun getWalletData() {


        walletList.clear()
        walletList.add("")
        walletList.add("")

        accDelegateWalletAdapter =
            AccDelegateWalletAdapter(
                context!!,
                walletList,
                this
            )

        accDelegateWalletRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = accDelegateWalletAdapter

        }

    }

    override fun onItemClick(position: Int) {
        Log.d("clickWallet" , "${walletList[position]}")
        val editDebtsSheet = EditDebtsSheet()
     //   bottomSheetActions.arguments = bundle
        editDebtsSheet.show(activity?.supportFragmentManager!!, "bottomSheetActions")

    }
}