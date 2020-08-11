package com.example.controllersystemapp.admin.storesproducts.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.ModelStringID
import com.example.controllersystemapp.R
import com.example.controllersystemapp.ViewModelHandleChangeFragmentclass
import com.example.util.UtilKotlin


class ResponsiblePersonFragment : Fragment() {

    lateinit var model : ViewModelHandleChangeFragmentclass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_responsible_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UtilKotlin.declarViewModel(this)!!


        model.setStringData(ModelStringID("aya" , 1))
    }
}