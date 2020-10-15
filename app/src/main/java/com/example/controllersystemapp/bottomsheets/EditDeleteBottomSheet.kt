package com.example.controllersystemapp.bottomsheets

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import com.example.controllersystemapp.ModelStringID
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.settings.expenses.ExpensesFragment.Companion.EXPENSES_ID
import com.example.controllersystemapp.admin.interfaces.DeleteUpdateListener
import com.example.controllersystemapp.admin.settings.admin.AdminPresenter
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.NameUtils.ADMIN_ID
import com.example.util.NameUtils.DELETE
import com.example.util.NameUtils.UPDATE
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_edit_delete_bottom_sheet.*


class EditDeleteBottomSheet : BottomSheetDialogFragment() {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_edit_delete_bottom_sheet, container, false)
    }
   // private var mListener: DeleteUpdateListener? = null

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mListener = if (context is DeleteUpdateListener) {
//            context
//        } else {
//            throw RuntimeException(
//                context.toString()
//                    .toString() + " must implement ItemClickListener"
//            )
//        }
//
//
//        try {
//            mListener = parentFragment as DeleteUpdateListener?
//        } catch (e: Exception) {
//            //handle exception
//            Log.d("exception" , "exc")
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // MyUtils.setCorners(view = lang_card!!, topLeft = 60f, topRight = 60f)

        deleteText?.setOnClickListener{

            //mListener?.onDeleteClick()
            model.setUpdateOrDelete(DELETE)
            dismiss()
        }
        updateText?.setOnClickListener{
            //mListener?.onUpdateClick()
            model.setUpdateOrDelete(UPDATE)

            dismiss()
        }
        closeSheet?.setOnClickListener{
            dismiss()

        }
    }


    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): EditDeleteBottomSheet {
            return EditDeleteBottomSheet()
        }
    }





}