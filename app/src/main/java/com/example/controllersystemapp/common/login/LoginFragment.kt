package com.example.controllersystemapp.common.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.AdminHomeActivity
import com.example.controllersystemapp.admin.reports.ReportsDetailsFragment
import com.example.controllersystemapp.common.forgetpassword.ForgetPassword
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton?.setOnClickListener {

            startActivity(Intent(context , AdminHomeActivity::class.java))

        }


        forgetPassword?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! , ForgetPassword() , "ForgetPassword"  , null , R.id.container)

            //UtilKotlin.changeFragment(ForgetPassword(), activity?.supportFragmentManager!!, R.id.container)

        }
    }

    companion object {
    }
}