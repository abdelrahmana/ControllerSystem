package com.smartangle.controllersystemapp.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.smartangle.controllersystemapp.R
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils.DELETE
import com.smartangle.util.NameUtils.UPDATE
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
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