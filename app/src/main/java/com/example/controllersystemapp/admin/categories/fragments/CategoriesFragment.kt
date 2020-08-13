package com.example.controllersystemapp.admin.categories.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.adapters.CategoriesAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.DelegatesAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.models.DelegatesModel
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_delegates.*

class CategoriesFragment : Fragment() , OnRecyclerItemClickListener {

    lateinit var categoriesAdapter: CategoriesAdapter
    var categoryList = ArrayList<Any>()
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_categories, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back?.setOnClickListener {

            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("back" , "Delegate Resume")

        getCateogriesData()
    }

    private fun getCateogriesData() {

        categoryList.clear()
        categoryList.add("")
        categoryList.add("")
        categoryList.add("")
        categoryList.add("")

        categoriesAdapter = CategoriesAdapter(categoryList , this)
        categoriesRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = categoriesAdapter
        }

    }


    override fun onItemClick(position: Int) {
        Log.d("click" , "category")
    }

}