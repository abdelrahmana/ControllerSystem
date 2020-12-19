package com.smartangle.controllersystemapp.accountant.noticesandreports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartangle.controllersystemapp.R
import com.smartangle.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_notices_and_reports.*


class NoticesAndReportsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notices_and_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backNoticesIcon?.setOnClickListener {
            activity?.let {
                if (it.supportFragmentManager.backStackEntryCount == 1)
                {
                    it.finish()
                }
                else{
                    it.supportFragmentManager.popBackStack()
                }
            }

        }

        addNotificationBtn?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! ,
                AddNoticesReportsFragment(), ""  ,
                null , R.id.redirect_acc_fragments)

        }

    }
}