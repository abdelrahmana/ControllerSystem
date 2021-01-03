package com.smartangle.controllersystemapp.admin.settings.payments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartangle.controllersystemapp.R
import kotlinx.android.synthetic.main.fragment_add_payment.*

class AddPaymentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageAddPayment?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }






    }
}