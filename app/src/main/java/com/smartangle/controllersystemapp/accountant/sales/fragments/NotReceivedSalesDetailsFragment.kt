package com.smartangle.controllersystemapp.accountant.sales.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartangle.controllersystemapp.R
import kotlinx.android.synthetic.main.fragment_not_received_sales_details.*


class NotReceivedSalesDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_received_sales_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close_img?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
    }
}