package com.example.controllersystemapp.common.forgetpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.common.verficationfragment.VerficationFragment
import com.example.util.UtilKotlin
import com.example.util.UtilKotlin.checkOViewsAvaliablity
import kotlinx.android.synthetic.main.fragment_forget_password.*

class ForgetPassword : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VALIDATION_CODE_Phone) {
            UtilKotlin.changeFragmentBack(
                activity!!,
                AddNewPasswordFragment(),
                "verficationFragment",
                null,
                R.id.container
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendButton?.setOnClickListener{
            if (checkOViewsAvaliablity(phoneNumberEditText,getString(R.string.phone_is_required),activity!!,errorPhone)) {
                errorPhone.visibility = View.GONE
                UtilKotlin.startValidationFragmentForResult(this@ForgetPassword ,
                    REQUEST_VALIDATION_CODE_Phone, activity!!,
                    phoneNumberEditText.text.toString())
            }else {
                errorPhone.visibility = View.VISIBLE

            }
        }

        backButton?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }
    }

    companion object {
        val REQUEST_VALIDATION_CODE_Phone = 103
    }
}