package com.example.controllersystemapp.admin.settings.editprofile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.util.UtilKotlin
import com.example.util.UtilKotlin.showSnackErrorInto
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.IOException

class EditProfileFragment : Fragment() {


    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backProfile?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        profileImg?.setOnClickListener {
            if (UtilKotlin.checkPermssionGrantedForImageAndFile(
                    activity!!,
                    UtilKotlin.permissionForImageAndFile,
                    this
                )
            ) {
              setDialogChooser()

            }
        }


    }

    private fun setDialogChooser() {
        val pictureDialog =
            AlertDialog.Builder(context)
        //pictureDialog.setTitle("Select Action");
        //pictureDialog.setTitle("Select Action");
        val pictureDialogItems = arrayOf(
            getString(R.string.choose_from_mobile_pic),
            getString(R.string.take_photo)
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == UtilKotlin.permissionForImageAndFile) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setDialogChooser()

            } else {
                showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        }
    }

    private val GALLERY = 1
    private  var CAMERA:Int = 2
    var bitmap: Bitmap? = null

    fun choosePhotoFromGallary() {

        UtilKotlin.performImgPicAction(GALLERY,this,activity!!)
      //  startActivityForResult(galleryIntent, GALLERY)
    }
    private fun takePhotoFromCamera() {

        UtilKotlin.performImgPicAction(CAMERA,this,activity!!)

        //  startActivityForResult(intent, CAMERA)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentURI = data.data
                try {
                    bitmap = BitmapFactory.decodeStream(
                        activity!!.contentResolver.openInputStream(contentURI!!)
                    )

                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    //String pjj = saveImage(bitmap);
//                    Log.d("dd" , "d "+pjj);
                    // Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    Log.d("dd" , "d ")
                    //changeImage = true
                    Log.d("dd" , "dff ")

                    profileImg?.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    //Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {

            bitmap = data!!.extras!!["data"] as Bitmap?
           // changeImage = true
            profileImg?.setImageBitmap(bitmap)
            // imgPath = saveImage(thumbnail);
            //fetchDataProfile(imgPath);

//            saveImage(thumbnail);
            //Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
}