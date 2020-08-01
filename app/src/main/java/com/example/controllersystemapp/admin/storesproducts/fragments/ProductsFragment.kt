package com.example.controllersystemapp.admin.storesproducts.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnItemClickListener
import com.example.controllersystemapp.admin.storesproducts.adapters.ProductsAdapter
import com.example.controllersystemapp.admin.storesproducts.models.ProductsModel
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : Fragment()  , OnItemClickListener {


    lateinit var rootView: View
    var productList = ArrayList<ProductsModel>()
    lateinit var productsAdapter: ProductsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_products, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()

        getProductsData()


    }

    private fun getProductsData() {

        productsRecycler.visibility = View.VISIBLE
        //noProductData.visibility = View.VISIBLE
        productList.clear()
        productList.add(ProductsModel("سامسونغ غالاكسي إس" , "950" , "رس" ,2 , "مخزن الكترونيات / متجر 1" , 0))
        productList.add(ProductsModel("سماعات" , "30" , "رس" ,2 , "مخزن الكترونيات / مخزن 1" , 0))
        productList.add(ProductsModel("كاميرا ديجيتال" , "1950" , "رس" ,2 , "مخزن الكترونيات / مخزن 1" , 1))
        productList.add(ProductsModel("كاميرا ديجيتال" , "1950" , "رس" ,2 , "مخزن الكترونيات / مخزن 1" , 1))

        productsAdapter = ProductsAdapter(context!! , productList , this)

        productsRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = productsAdapter

        }




    }

    override fun onItemClick(position: Int) {

        //Toast.makeText(context!! , "pos "+productList[position].name , Toast.LENGTH_LONG).show()
        Log.d("click" , "position $position name ${productList[position].name}")

    }


}