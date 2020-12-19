package com.smartangle.util

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.admin.addproduct.AddProductFragment.Companion.GALLERY
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantDetailsResponse
import com.smartangle.controllersystemapp.admin.settings.admin.AdminDetailsResponse
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientDetailsResponse
import com.smartangle.controllersystemapp.admin.storesproducts.models.ProductsDetailsResponse
import com.smartangle.controllersystemapp.admin.storesproducts.models.StoreDetailsResponse
import com.smartangle.controllersystemapp.common.ContainerActivityForFragment
import com.smartangle.controllersystemapp.common.verficationfragment.ValidationActivity
import com.smartangle.controllersystemapp.common.verficationfragment.VerficationFragment.Companion.phoneNumberKey
import com.smartangle.util.ApiConfiguration.ErrorBodyResponse
import com.smartangle.util.PrefsUtil.getSharedPrefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.loader_layout.*
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


object UtilKotlin {
    fun changeFragment(targetFragment: Fragment, fragmentManger : FragmentManager, id : Int) { // fragment no back
        fragmentManger
            .beginTransaction()
            .replace(id, targetFragment, "fragment")
            .setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out)
            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

    }

    val permissionForImageAndFile = 200
    val submitPermssion = 190 // when user submit check this permssion before creating file and go to update profile
    val permissionScan = 330




    fun getCreatedFileFromBitmap(fileName: String, bitmapUpdatedImage: Bitmap, typeOfFile : String?, context:Context) : File {
        val bytes =  ByteArrayOutputStream()
        bitmapUpdatedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        /*   val f = new File(Environment.getExternalStorageDirectory()
                   + File.separator + "testimage.jpg");*/
        val f =  initFile(fileName,typeOfFile?:"jpg",context)
        f?.createNewFile()
        val fo =  FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()

        return f!!
    }
    fun localSignOut(context: Activity?) {
        PrefsUtil.removeKey(context!! , PrefsModel.TOKEN)
        PrefsUtil.setLoginState(context!!, false)
        PrefsUtil.removeKey(context!! , PrefsModel.userModel)
        //   context.startActivity(Intent(context, AuthContainer::class.java))
        //  context!!.finishAffinity()
        context.startActivity(Intent(context, ContainerActivityForFragment::class.java)) // go to home please
        // startActivity(Intent(activity,AuthContainer::class.java))
        context!!.finishAffinity()
    }

    fun checkIfInvalidToken(code : Int,activity: Activity): Boolean {
        if (code == 401) // invalid token
        {
            localSignOut(activity)
            return true // in invalid
        }
        return false // valid
    }


    fun performImgPicAction(which: Int, fragment: Fragment?, context: Activity) {
        var intent: Intent?
        Log.d("which" , "fun $which")
        if (which == GALLERY) {  // in case we need to get image from gallery
            intent = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        } else {
            // in case we need camera
            intent = Intent()
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE)
        }
        if (fragment != null)
        {
            fragment.startActivityForResult(intent, which)
        }
        else
        {
            context.startActivityForResult(intent, which)
        }
    }

    fun checkPermssionGrantedForImageAndFile(context: Activity,requestCode :Int,fragment: Fragment?) : Boolean {
        var allow = false
        // imageView.setOnClickListener {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (fragment!=null) // in fragment
                    fragment.requestPermissions(arrayOf(
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE),
                        requestCode) // request permission now
                else // in activity
                    context.requestPermissions(arrayOf(
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE),
                        requestCode) // request permission now
            }else{
                allow = true

            }
        } else {
            // startCameraNow()
            allow =  true

        }

        //  }
        return allow
    }

    fun getCreatedFileMultiPartFromBitmap(fileName: String, bitmapUpdatedImage: Bitmap?, typeOfFile : String?, context:Context) : File {
        val bytes =  ByteArrayOutputStream()
        bitmapUpdatedImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        /*   val f = new File(Environment.getExternalStorageDirectory()
                   + File.separator + "testimage.jpg");*/
        val f =  initFile(fileName,typeOfFile?:"jpg",context)
        f?.createNewFile()
        val fo =  FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()

        return f!!
    }

    fun initFile(name :String,type : String,context: Context): File? {  // to delete file you need to get the absoloute paths for it and it's directory
        var file : File? = null // creating file for video
        file = File( context.cacheDir.absolutePath, SimpleDateFormat(
            "'$name'yyyyMMddHHmmss'.$type'").format(Date()))
        //}
        return file
    }
    fun ProgressDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        val view = (context as Activity).layoutInflater.inflate(R.layout.loader_layout, null)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.loaderContainer?.visibility = View.VISIBLE
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return dialog
    }

    fun declarViewModel(activity: Fragment?): ViewModelHandleChangeFragmentclass? { // listen life cycle to fragment only
        return ViewModelProvider(activity!!).get(ViewModelHandleChangeFragmentclass::class.java)
    }
    fun declarViewModel(activity: FragmentActivity?): ViewModelHandleChangeFragmentclass? {
        return ViewModelProvider(activity!!).get(ViewModelHandleChangeFragmentclass::class.java)
    }

    fun getLocalLanguage(context: Context): String? {
        val locale: Locale
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
        return locale.language
    }
    fun setRecycleView(recyclerView: RecyclerView?, adaptor: RecyclerView.Adapter<*>,
                       verticalOrHorizontal: Int?, context:Context, gridModel: GridModel?, includeEdge : Boolean) {
        var layoutManger : RecyclerView.LayoutManager? = null
        if (gridModel==null) // normal linear
            layoutManger = LinearLayoutManager(context, verticalOrHorizontal!!,false)
        else
        {
            layoutManger = GridLayoutManager(context, gridModel.numberOfItems)
            layoutManger.isAutoMeasureEnabled = true
            if (recyclerView?.itemDecorationCount==0)
                recyclerView?.addItemDecoration(SpacesItemDecoration(gridModel.numberOfItems, gridModel.space, includeEdge))
        }
        recyclerView?.apply {
            setLayoutManager(layoutManger)
            setHasFixedSize(true)
            adapter = adaptor

        }
    }
    fun checkOViewsAvaliablity(view: EditText?, errorMessage: String, activity: Activity, errorMedicalInssurance: TextView?): Boolean { // errors on view which is required
        if (checkAvalibalityOptions(view?.text.toString())==true) // not empty
        {
            errorMedicalInssurance?.text = "" // clean it
            errorMedicalInssurance?.visibility = View.GONE

            return true
        }
        else {
            errorMedicalInssurance?.text = errorMessage
            errorMedicalInssurance?.visibility = View.VISIBLE
        }
        // showSnackErrorInto(activity!!, errorMessage)
        return false


    }
    fun checkAvalibalityOptions(checkEmptyOrNot: Any) :Boolean? { // when check empty strings or int or whatever you need
        /* when(checkEmptyOrNot) {
             Int->{
                 return (checkEmptyOrNot as Int)>0
             }
             String->{
                 return (checkEmptyOrNot as String).isNotEmpty()

             }
         }*/
        if (checkEmptyOrNot is Int) {
            return (checkEmptyOrNot as Int)>0
        }
        else if (checkEmptyOrNot is String)
            return (checkEmptyOrNot as String).isNotEmpty()
        else if (checkEmptyOrNot is ArrayList<*>)
            return (checkEmptyOrNot).size>0
        return false // no supports value
    }
    fun changeFragmentWithBack(activity: FragmentActivity?, containerBottomNav: Int, fragment: Fragment, bundle: Bundle?) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        transaction.replace(
            containerBottomNav ,
            fragment
        )
        transaction.addToBackStack(null)
        transaction.commit()


    }
    fun setLanguagePerActivity(activity : Activity, intent: Intent?){
        val currentLanguage = getSharedPrefs(
            activity
        ).getString(PrefsModel.localLanguage, "ar")?:"ar"
        //  if (UtilKotlin.getSharedPrefs(activity).getString(PrefsModel.localLanguage, "en").equals("en")) {
        val locale = Locale(currentLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        activity.getResources().updateConfiguration(config, activity.resources.displayMetrics)

        setApplicationlanguage(
            activity,
            currentLanguage
        )
        // add current language if default
        getSharedPrefs(activity).edit().putString(PrefsModel.localLanguage, currentLanguage).apply()
        if (intent!=null) // we need to start activity
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity.finishAffinity()
            activity.startActivity(intent) // start redirect activity when you set it
        }
    }
    private fun setApplicationlanguage(context: Context, language: String?) {
        val res = context.applicationContext.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        conf.setLocale(Locale(language!!)) // API 17+ only.
        /*  } else {
              conf.locale = Locale(language)
          }

         */
        res.updateConfiguration(conf, dm)
    }

    fun replaceFragmentWithBack(
        context: Context, currentFragment: Fragment, newFragment: Fragment,
        bundle: Bundle?, id: Int, requestCode: Int, flag: Boolean?, back: Boolean?
    ) {
        //(context as FragmentActivity)
        Handler().post {
            val manager =
                (context as FragmentActivity).supportFragmentManager// newFragment.fragmentManager
            val ft = manager!!.beginTransaction()
            if (bundle != null) {
                newFragment.arguments = bundle
            }
            if (flag!!) {
                /*   ft.setCustomAnimations(R.anim.slide_in_right,
                           R.anim.slide_out_right, R.anim.translat_right,
                           R.anim.translat_left)*/
            } else {
                ft.setCustomAnimations(0, 0)
            }
            ft.replace(id, newFragment, newFragment.javaClass.getName())
            if (back!!) {
                ft.addToBackStack(null)
                newFragment.setTargetFragment(currentFragment, requestCode)
            }
            ft.commit()
        }
    }


    fun changeFragmentBack(activity: FragmentActivity, fragment: Fragment, tag: String ,bundle: Bundle?, id : Int ) {

        val transaction = activity?.supportFragmentManager.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        //R.id.frameLayout_direction+
        transaction.replace(id, fragment, tag)
        //transaction.addToBackStack(tag)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun setUnderLine(textValues: String, textView: TextView?) {
        val content = SpannableString(textValues)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        textView?.setText(content)

    }

    fun startValidationFragmentForResult(parentFragment: Fragment?, requestValidationCode: Int,context: Activity,phoneNumber : String) {
        PrefsUtil.getSharedPrefs(context).edit().putString(phoneNumberKey,/*"+"+*/phoneNumber).apply() // set the required phone number
        if (parentFragment != null)
            parentFragment.startActivityForResult(Intent(context, ValidationActivity::class.java), requestValidationCode)
        else
            context.startActivityForResult(Intent(context,ValidationActivity::class.java),requestValidationCode)
    }
    fun hideKeyboard(view: View) {
        val input = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

    }

    fun hideKeyboardEditText(editText: EditText, view: View) {

        val hide: InputMethodManager? =
            view.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        hide!!.hideSoftInputFromWindow(editText.windowToken, 0)

    }

    fun getFormatedDate(dateZone : String,context: Context,dateOrTime : Boolean=false): String { // return date or time
        val calendar = Calendar.getInstance()
        val input =
            SimpleDateFormat("yyyy-MMMMM-dd'T'HH:mm:ss.SSS'Z'", Locale(getLocalLanguage(context)?:"ar"))
        val output = SimpleDateFormat("dd/MM/yyyy", Locale(getLocalLanguage(context)?:"ar"))
        val timeFormatter = SimpleDateFormat("h:mm a", Locale(getLocalLanguage(context)?:"ar"))
        val parser = SimpleDateFormat("d MMMMM yyyy h:mm a",Locale(getLocalLanguage(context)?:"ar"))
        var d: Date? = null
        try {
            d = input.parse(dateZone)
        } catch (e: Exception) {
        }


        //        if (dateOrTime) // true date else time
        return parser.format(d?: Date())
        // else return  timeFormatter.format(d)

    }
    fun isNetworkAvailable(context: Context): Boolean {
        var netstate = false
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        netstate = true
                        break
                    }
                }
            }
        }
        return netstate
    }

    fun showSnackErrorInto(
        activity: Activity?,
        error: String?
    ) {
        if (activity != null) {
            Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .title(activity.getString(R.string.error))
                .message(error!!)
                .backgroundColorRes(R.color.md_red_600)
                .dismissOnTapOutside()
                .duration(2500)
                .enableSwipeToDismiss()
                .enterAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(550)
                        .alpha()
                        .overshoot()
                )
                .exitAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(200)
                        .anticipateOvershoot()
                )
                .build().show()
        }
    }

    fun getDeviceId(context: Context): String? {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }


    fun getErrorBodyResponse(errorBody: ResponseBody?, context: Context): String {
        var errortext = ""
        val gson = Gson()
        val type = object : TypeToken<ErrorBodyResponse>() {}.type
        var errorResponse: ErrorBodyResponse? = gson.fromJson(errorBody?.charStream(), type)?:(ErrorBodyResponse())
//            .also {
//            it.msg.add(context.getString(R.string.error))
//
//
//        })

        for (i in errorResponse?.msg!!.indices) {
            Log.e("ResponseForError", "error ${errorResponse?.msg?.get(i)}")

            errortext = errortext + errorResponse?.msg?.get(i) + "\n"

        }
        // var error =errorResponse?.msg?.toString()
        //  error = error?.substring(1,error.length- 1)
        //var error= errorResponse?.message
        return errortext?:"" /*errorResponse?.msg.toString().apply{
      replace("[\\[\\](){}]","")/*replace("[","")
          .replace("]","")*/}*/
    }

    fun showSnackMessage(activity: Activity?,  messageBody: String) {
        if (messageBody.equals("timeout", ignoreCase = true)) {
            return
        }
        activity?.let {

            /*    if (error.contains("resolve")) {
                error = activity.getString(R.string.network_error_value)
            }*/
            //.title(title ?: "")
            Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .message(messageBody)
                .backgroundColorRes(R.color.green_message)
                .duration(2500)
                .dismissOnTapOutside()
                .enableSwipeToDismiss()
                .enterAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(550)
                        .alpha()
                        .overshoot()
                )
                .exitAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(200)
                        .anticipateOvershoot()
                )
                .build().show()
        }
    }

    fun getDelegateCallCenter(jsonString : String): CallCenterDelegateData? { // this should return the object
        val jso = jsonString
        val gson = Gson()
        val typeToken = object : TypeToken<CallCenterDelegateData?>() {}.type
        val obj = gson.fromJson<CallCenterDelegateData>(jso, typeToken) ?: CallCenterDelegateData() //ResponseLogin(Data("", null))
        return obj

    }

    fun getProductDetails(productString: String) : ProductsDetailsResponse?{
        val gson = Gson()
        val typeToken = object : TypeToken<ProductsDetailsResponse?>() {}.type
        val obj = gson.fromJson<ProductsDetailsResponse>(productString, typeToken) ?: ProductsDetailsResponse(null) //ResponseLogin(Data("", null))
        return obj
    }

    fun getStoreDetails(storeString: String) : StoreDetailsResponse?{
        val gson = Gson()
        val typeToken = object : TypeToken<StoreDetailsResponse?>() {}.type
        val obj = gson.fromJson<StoreDetailsResponse>(storeString, typeToken) ?: StoreDetailsResponse(null) //ResponseLogin(Data("", null))
        return obj
    }

    fun getAccountantDetails(accountantString: String) : AccountantDetailsResponse?{
        val gson = Gson()
        val typeToken = object : TypeToken<AccountantDetailsResponse?>() {}.type
        val obj = gson.fromJson<AccountantDetailsResponse>(accountantString, typeToken) ?: AccountantDetailsResponse(null) //ResponseLogin(Data("", null))
        return obj
    }

    fun getAdminDetails(adminString: String) : AdminDetailsResponse?{
        val gson = Gson()
        val typeToken = object : TypeToken<AdminDetailsResponse?>() {}.type
        val obj = gson.fromJson<AdminDetailsResponse>(adminString, typeToken) ?: AdminDetailsResponse(null) //ResponseLogin(Data("", null))
        return obj
    }

    fun getClientDetails(clientString: String) : ClientDetailsResponse?{
        val gson = Gson()
        val typeToken = object : TypeToken<ClientDetailsResponse?>() {}.type
        val obj = gson.fromJson<ClientDetailsResponse>(clientString, typeToken) ?: ClientDetailsResponse(null) //ResponseLogin(Data("", null))
        return obj
    }

}