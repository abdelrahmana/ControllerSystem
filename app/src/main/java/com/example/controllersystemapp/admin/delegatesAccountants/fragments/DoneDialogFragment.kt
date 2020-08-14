package com.example.controllersystemapp.admin.delegatesAccountants.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import kotlinx.android.synthetic.main.fragment_done_dialog.*

class DoneDialogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_done_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        closeDialog?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
    }
}