package com.smartangle.controllersystemapp.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.smartangle.controllersystemapp.R
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_role_bottom_sheet.*


class RoleBottomSheet : BottomSheetDialogFragment() {

    lateinit var model : ViewModelHandleChangeFragmentclass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = UtilKotlin.declarViewModel(activity!!)!!

        return inflater.inflate(R.layout.fragment_role_bottom_sheet, container, false)
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // MyUtils.setCorners(view = lang_card!!, topLeft = 60f, topRight = 60f)

        closeRoleSheet?.setOnClickListener {
            dismiss()
        }
        adminText?.setOnClickListener {
            model?.setInteDataVariable(1)
            dismiss()
        }

        accountantText?.setOnClickListener {
            model?.setInteDataVariable(2)
            dismiss()

        }

        callCenterText?.setOnClickListener {
            model?.setInteDataVariable(3)
            dismiss()
        }

        delegateText?.setOnClickListener {
            model?.setInteDataVariable(4)
            dismiss()
        }



    }



    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): RoleBottomSheet {
            return RoleBottomSheet()
        }
    }

}