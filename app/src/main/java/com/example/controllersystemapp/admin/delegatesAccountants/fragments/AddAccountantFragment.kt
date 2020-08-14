package com.example.controllersystemapp.admin.delegatesAccountants.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.util.NameUtils.WHICH_ADDED
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_add_accountant.*

class AddAccountantFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_accountant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImgAddAccountant?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        confirmAddAccountantBtn?.setOnClickListener {

            val bundle = Bundle()
            bundle.putString(WHICH_ADDED , getString(R.string.done_add_accountant))

            UtilKotlin.changeFragmentBack(activity!! ,DoneDialogFragment() , "DoneAddAccountant" , bundle)
        }
    }
}