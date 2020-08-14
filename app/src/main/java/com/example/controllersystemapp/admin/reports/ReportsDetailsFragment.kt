package com.example.controllersystemapp.admin.reports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import kotlinx.android.synthetic.main.fragment_orders_delivery.*


class ReportsDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeImg?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

    }
}