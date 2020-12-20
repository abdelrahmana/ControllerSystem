package com.smartangle.controllersystemapp.admin.settings.payments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartangle.controllersystemapp.R
import com.smartangle.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_payments.*
import kotlinx.android.synthetic.main.no_fees.view.*

class PaymentsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noPaymentsData?.noDataText?.text = getString(R.string.no_payment_exist)

        backImagePayment?.setOnClickListener {

            if (activity?.supportFragmentManager?.backStackEntryCount == 1) {
                activity?.finish()
            } else {
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        addPaymentBtn?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! ,
                AddPaymentFragment(), "AddPayment" , null,R.id.frameLayout_direction)

        }
    }
}