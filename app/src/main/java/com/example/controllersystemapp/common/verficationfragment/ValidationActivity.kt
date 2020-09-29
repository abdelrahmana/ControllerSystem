package com.example.controllersystemapp.common.verficationfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.controllersystemapp.R
import com.example.util.CommonActivity
import com.example.util.UtilKotlin


class ValidationActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validation)
         setValidationFragment()

    }

    private fun setValidationFragment() {
         // phone is snet in util as prefs
      /*  val whichFragment = intent.getStringExtra(AuthContainer.whichFragment)?:AuthContainer.loginFragment
        val validation = ValidatePhoneFragment()
        val bundle = Bundle()
        bundle.putString(AuthContainer.whichFragment,whichFragment)
        validation.arguments = bundle*/
        UtilKotlin.changeFragment(VerficationFragment(),supportFragmentManager,R.id.containerValidation)
    }
}
