package com.example.controllersystemapp.common.verficationfragment

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.controllersystemapp.R
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.verfication_fragment.*


class VerficationFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.verfication_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UtilKotlin.setUnderLine(getString(R.string.send_ramz),sendAgain)
        backButton?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }
    }
}