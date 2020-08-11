package com.example.controllersystemapp.admin.storesproducts.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.controllersystemapp.ModelStringID
import com.example.controllersystemapp.R
import com.example.controllersystemapp.ViewModelHandleChangeFragmentclass
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AddAccountantFragment
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_add_store.*


class AddStoreFragment : Fragment() {


    lateinit var model : ViewModelHandleChangeFragmentclass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = UtilKotlin.declarViewModel(this)!!

        responsiblePersonContainer?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! , ResponsiblePersonFragment() , "ResponsiblePerson" )

        }

        model.stringNameData.observe(activity!!, Observer {


        })

    }


}