package com.smartangle.controllersystemapp.admin.makeorders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.ModelStringID
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.storesproducts.fragments.ResponsiblePersonFragment
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_admin_make_order.*


class AdminMakeOrderFragment : Fragment() {

    lateinit var model: ViewModelHandleChangeFragmentclass

    var cleintName = ""
    var clientID: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = UtilKotlin.declarViewModel(activity!!)!!

        return inflater.inflate(R.layout.fragment_admin_make_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageOrder?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1) {
                activity?.finish()
            } else {
                activity?.supportFragmentManager?.popBackStack()
            }
        }


        creditorCheckImg?.setOnClickListener {
            creditorCheckImg?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_radio_check
                )
            )
            cashCheckImg?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_radio_not_check
                )
            )
            valuePaidContainer?.visibility = View.VISIBLE
        }

        cashCheckImg?.setOnClickListener {
            cashCheckImg?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_radio_check
                )
            )
            creditorCheckImg?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_radio_not_check
                )
            )
            valuePaidContainer?.visibility = View.GONE
        }


        customerSelectCard?.setOnClickListener {

            UtilKotlin.changeFragmentBack(
                activity!!,
                CustomersSpecifyFragment(), "CustomersSpecify",
                null, R.id.frameLayout_direction
            )

//            UtilKotlin.changeFragmentBack(
//                activity!!,
//                ResponsiblePersonFragment(), "ResponsiblePerson",
//                null, R.id.frameLayout_direction
//            )


        }


        model.stringNameData.observe(activity!!,
            Observer<ModelStringID> { modeStringId ->
                Log.d("modelOrder", "dataaa")

                if (modeStringId != null) {

                    //setData(personName)
                    Log.d("modelOrder", "name ${modeStringId.name}")
                    Log.d("modelOrder", "id ${modeStringId.id}")
                    cleintName = modeStringId.name
                    clientID = modeStringId.id ?: -1
                    //model.setStringData(null)

                }

            })

    }

    override fun onResume() {
        super.onResume()
        Log.d("modelOrder", "resume")
        customerSelectText?.text = cleintName?:""
        model.setStringData(null)

    }


    override fun onDestroyView() {
        model.let {
            it.stringNameData.removeObservers(activity!!)

        }
        super.onDestroyView()

    }
}