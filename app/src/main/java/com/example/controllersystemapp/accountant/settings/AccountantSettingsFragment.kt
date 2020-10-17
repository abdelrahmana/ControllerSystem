package com.example.controllersystemapp.accountant.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.settings.expenses.ExpensesFragment
import com.example.controllersystemapp.admin.settings.EditPasswordFragment
import com.example.controllersystemapp.admin.settings.editprofile.EditProfileFragment
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_accountant_settings.*


class AccountantSettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accountant_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImage?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }


        expensesCard?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! ,
                ExpensesFragment(), "ExpensesFragment" , null,R.id.redirect_acc_fragments)
        }


        editProfileCard?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! ,
                EditProfileFragment(), "" , null,R.id.redirect_acc_fragments)
        }

        editPasswordCard?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! ,
                EditPasswordFragment(), "" , null,R.id.redirect_acc_fragments)
        }

        logOutCard?.setOnClickListener {
           Log.d("logout" , "click")
        }
    }
}