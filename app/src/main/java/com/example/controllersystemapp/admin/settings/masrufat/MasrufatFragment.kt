package com.example.controllersystemapp.admin.settings.masrufat

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
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_masrufat.*

class MasrufatFragment : Fragment() , ClickAcceptRejectListener {

    lateinit var masrufatAdapter: MasrufatAdapter
    var arrayList = ArrayList<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_masrufat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageFees?.setOnClickListener {
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

        getDataList()

    }

    private fun getDataList() {

        arrayList.clear()
        for (i in 0..4)
        {
            arrayList.add("")
        }

        masrufatAdapter = MasrufatAdapter(arrayList , this)
        masrufatRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = masrufatAdapter
        }


    }


    override fun onItemListClick(position: Int) {
        UtilKotlin.changeFragmentBack(activity!! ,
            FeesDetailsFragment(), "FeesDetails" , null)

    }

    override fun onAcceptClick(position: Int) {
        Log.d("click" , "accept")
    }

    override fun onRejectClick(position: Int) {
        Log.d("click" , "reject")
    }
}