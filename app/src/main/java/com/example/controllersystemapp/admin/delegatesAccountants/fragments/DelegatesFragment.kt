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
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.DelegatesAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.models.DelegatesModel
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_delegates.*

class DelegatesFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var delegatesAdapter: DelegatesAdapter
    var delegatesList = ArrayList<DelegatesModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delegates, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("back" , "Delegate crested")


    }

    override fun onResume() {
        super.onResume()
        Log.d("back" , "Delegate Resume")

        getDeleagtesData()
    }

    private fun getDeleagtesData() {

        delegatesList.clear()
        for (i in 0..4)
        {
            delegatesList.add(DelegatesModel("احمد حازم" , null , " +966 56784 9876" , i+1))
        }
        delegatesCount?.text = delegatesList.size.toString()

        delegatesAdapter = DelegatesAdapter(context!! , delegatesList , this)
        delegatesRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = delegatesAdapter
        }


    }

    override fun onItemClick(position: Int) {

        Log.d("clickDelegate" , "${delegatesList[position].Id}")

//        UtilKotlin.replaceFragmentWithBack(context!!, this, DelegateDetailsFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)

        UtilKotlin.changeFragmentBack(activity!! ,DelegateDetailsFragment() , ""  , null)
    }

}