package com.example.controllersystemapp.accountant.noticesandreports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_add_notices_reports.*

class AddNoticesReportsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_notices_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backAddNoticesIcon?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()
        }

        selectDelegateContainer?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! ,
                SelectDelegatesFragment(), ""  ,
                null , R.id.redirect_acc_fragments)

        }
    }
}