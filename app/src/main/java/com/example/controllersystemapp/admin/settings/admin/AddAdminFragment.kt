package com.example.controllersystemapp.admin.settings.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.DoneDialogFragment
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_add_admin.*


class AddAdminFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmAddAdmin?.setOnClickListener {

            val bundle = Bundle()
            bundle.putString(NameUtils.WHICH_ADDED, getString(R.string.done_add_admin))

            UtilKotlin.changeFragmentBack(activity!! ,
                DoneDialogFragment() , "DoneAddAccountant" , bundle)
        }

        backProfile?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

    }
}