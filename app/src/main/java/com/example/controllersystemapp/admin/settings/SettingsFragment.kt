package com.example.controllersystemapp.admin.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.BuildConfig
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AddAccountantFragment
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
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


        editProfileCard?.setOnClickListener{

            UtilKotlin.changeFragmentBack(activity!! , EditProfileFragment() , "EditProfile" , null)


        }

        adminCard?.setOnClickListener{
            UtilKotlin.changeFragmentBack(activity!! , AdminFragment() , "Admin" , null)
        }

        versionNumber?.text = "${versionNumber?.text} ${BuildConfig.VERSION_NAME}"
    }
}