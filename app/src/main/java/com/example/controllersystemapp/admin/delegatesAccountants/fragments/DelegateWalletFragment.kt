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
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.DelegateOrdersAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.DelegateWalletAdapter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.admin.storesproducts.models.ProductsModel
import kotlinx.android.synthetic.main.fragment_delegate_wallet.*
import kotlinx.android.synthetic.main.fragment_delegates_orders.*

class DelegateWalletFragment : Fragment() , OnRecyclerItemClickListener {

    lateinit var rootView: View
    var walletList = ArrayList<ProductsModel>()
    lateinit var delegateWalletAdapter: DelegateWalletAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_delegate_wallet, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun onResume() {
        super.onResume()

        getWalletData()

    }

    private fun getWalletData() {


        walletList.clear()
        walletList.add(ProductsModel("اسم المنتج", "1000" , "رس" ,2 , "وصف المنتج.." , 0))
        walletList.add(ProductsModel("اسم المنتج", "108" , "رس" ,2 , "وصف المنتج.." , 0))

        delegateWalletAdapter = DelegateWalletAdapter(context!! , walletList , this)

        delegateWalletRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = delegateWalletAdapter

        }

    }

    override fun onItemClick(position: Int) {
        Log.d("clickWallet" , "${walletList[position].name}")

    }

}