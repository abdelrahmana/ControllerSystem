package com.example.controllersystemapp.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.example.controllersystemapp.admin.storeclassification.StoreClassificationFragment
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_add_product.*


class AddProductFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        materialProduct?.setOnClickListener {
         UtilKotlin.changeFragmentBack(activity!! , FragmentProductclassification() , "productClassification"  ,
             null , R.id.frameLayout_direction)
        }

        materialSave?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! , StoreClassificationFragment() , "storeClassification"  ,
                null , R.id.frameLayout_direction)
        }
    }

    companion object {

    }
}