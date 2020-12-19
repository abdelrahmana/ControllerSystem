package com.smartangle.controllersystemapp.admin.settings.editprofile

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.settings.editprofile.EditPresenter.editProfile
import com.smartangle.controllersystemapp.common.login.LoginResponse
import com.smartangle.controllersystemapp.common.login.User
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.PrefsUtil
import com.smartangle.util.UtilKotlin
import com.smartangle.util.UtilKotlin.showSnackErrorInto
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.IOException
import java.util.*

class EditProfileFragment : Fragment() {


    lateinit var rootView: View
    var userModel : User? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userModel = PrefsUtil.getUserModel(context!!)?:User() // get user model then set it
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserModelForUsers()
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
        saveEditProfile?.setOnClickListener {
            if (UtilKotlin.checkPermssionGrantedForImageAndFile(
                    activity!!,
                    100,
                    this@EditProfileFragment
                )
            ) {
                submitEditProfile()
            }
        }


    }

    var webService : WebService?=null
    private fun submitEditProfile() {
        if (UtilKotlin.checkAvalibalityOptions(profileNameEdt.text.toString()) == true
            && UtilKotlin.checkAvalibalityOptions(mobileNameEdt.text.toString()) == true
            && UtilKotlin.checkAvalibalityOptions(addressEdt.text.toString()) == true
        ) {
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)

            builder.addFormDataPart(
                "name",
                ((profileNameEdt.text.toString()))/*.toRequestBody("multipart/form-data".toMediaTypeOrNull())*/
            )
            //  builder.addFormDataPart("place_id",((createEditPackageRequest.place_id?:0).toString())/*.toRequestBody("multipart/form-data".toMediaTypeOrNull())*/)
            builder.addFormDataPart(
                "long",
                (userModel?.long
                    ?: "0.0")/*.toRequestBody("multipart/form-data".toMediaTypeOrNull())*/
            )
            builder.addFormDataPart("lat", (userModel?.lat ?: "0.0"))
            builder.addFormDataPart("enable_notification", 1.toString())
            if (bitmap != null) {
                val f = UtilKotlin.getCreatedFileMultiPartFromBitmap(
                    "image",
                    bitmap!!,
                    "jpg",
                    context!!
                )

                builder.addFormDataPart(
                    "image",
                    f.name,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), f)
                )
            }
            editProfile(webService!!,ItemListObserver(),builder.build())

        }
    }
    var progressDialog : Dialog?=null
    var disposableObserver : DisposableObserver<Response<LoginResponse>>?=null
    private fun ItemListObserver(): DisposableObserver<Response<LoginResponse>> {

        disposableObserver= object : DisposableObserver<Response<LoginResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<LoginResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    PrefsUtil.setUserModel(context!!, response.body()?.data?.user)
                    UtilKotlin.showSnackMessage(activity!!, getString(R.string.updated_successfully))
                    Handler().postDelayed({
                        activity?.onBackPressed()
                    },750)
                }
                else
                {
                    progressDialog?.dismiss()
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
        return disposableObserver!!
    }
    private fun setUserModelForUsers() {
        profileNameEdt?.setText(userModel?.name?:"")
        mobileNameEdt?.setText(userModel?.phone?:"")
        Glide.with(context!!).load(userModel?.image?:"").dontAnimate()
            // .error(R.drawable.no_profile).placeholder(R.drawable.no_profile)
            .into(profileImg)
        if (!userModel?.lat.isNullOrEmpty()) {
            try {

                val address: Address? = Geocoder(context, Locale.getDefault()).getFromLocation(
                    (userModel?.lat ?: "0.0").toDouble(),
                    (userModel?.long ?: "0.0").toDouble(), 1
                )[0]
                addressEdt?.setText(address?.getAddressLine(0) ?: "") // set address
            }catch (e:Exception){

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
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setDialogChooser()

            } else {
                showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }

        }

        if (requestCode == 100) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                submitEditProfile()

            } else {
                showSnackErrorInto(activity, getString(R.string.cannot_edit))

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