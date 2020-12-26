package com.smartangle.controllersystemapp.accountant.delegatecallcenter

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.smartangle.controllersystemapp.R
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.acc_delegate_sheet.*


class AccDelegateDetailsBottomSheet : BottomSheetDialogFragment() {


    lateinit var model: ViewModelHandleChangeFragmentclass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.acc_delegate_sheet, container, false)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setContentView(R.layout.acc_delegate_sheet)
        model = UtilKotlin.declarViewModel(activity)!!

        dialog.setOnShowListener {
            val castDialog = it as BottomSheetDialog
            val bottomSheet = castDialog.findViewById<View?>(R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_DRAGGING
                    }
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }

        return dialog
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // MyUtils.setCorners(view = lang_card!!, topLeft = 60f, topRight = 60f)

//        if (arguments?.getBoolean(CallCenterFragment.callCenter,false) != true) // not call center
//        {
//            deleteAdminText.text = getString(R.string.delete_delegate)
//            blockAdminText?.text = getString(R.string.block_delegate)
//        }
        specialMessageDelegate?.setOnClickListener{
            //model.setNotifyItemSelected(1) // edit profile
            model.setNotifyItemSelected(ACCOUNTANT_MessageDelegate) //remove delegate

            dismiss()
        }
        deleteDelegateText?.setOnClickListener{
            model.setNotifyItemSelected(ACCOUNTANT_REMOVE_DELEGATE) //remove delegate
            dismiss()
        }
        blockDelegateText?.setOnClickListener{
            model.setNotifyItemSelected(ACCOUNTANT_BLOCK_Delegate) //remove delegate
            dismiss()
        }
        closeSheet?.setOnClickListener{
            dismiss()

        }


    }










    override fun onDismiss(dialog: DialogInterface) {


        super.onDismiss(dialog)
    }



    companion object{
        val ACCOUNTANT_REMOVE_DELEGATE = "accountantRemoveDelegate"
        val ACCOUNTANT_MessageDelegate = "messageDelegate"
        val ACCOUNTANT_BLOCK_Delegate = "acountantBlockDelegate"

    }



}