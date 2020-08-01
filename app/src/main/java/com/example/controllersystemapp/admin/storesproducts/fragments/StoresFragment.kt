package com.example.controllersystemapp.admin.storesproducts.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnItemClickListener
import com.example.controllersystemapp.admin.storesproducts.adapters.ProductsAdapter
import com.example.controllersystemapp.admin.storesproducts.adapters.StoresAdapter
import com.example.controllersystemapp.admin.storesproducts.models.ProductsModel
import com.example.controllersystemapp.admin.storesproducts.models.StoresModel
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.android.synthetic.main.fragment_stores.*


class StoresFragment : Fragment() , OnItemClickListener {

    lateinit var rootView: View
    var storeList = ArrayList<StoresModel>()
    lateinit var storesAdapter: StoresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_stores, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()

        getStoresData()


    }

    private fun getStoresData() {

        storesRecycler?.visibility = View.VISIBLE
        //noStoresData.visibility = View.VISIBLE
        storeList.clear()
        for (i in 0..3)
        {
            storeList.add(StoresModel("مخزن ${i+1}" , "الرياض ، عاصمة المملكة العربية السعودية " , "فيصل الرابحي" , i+1))
        }
        storesAdapter = StoresAdapter(context!! , storeList , this)

        storesRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = storesAdapter

        }


    }

    override fun onItemClick(position: Int) {
        Log.d("click" , "position $position name ${storeList[position].name}")

    }


}