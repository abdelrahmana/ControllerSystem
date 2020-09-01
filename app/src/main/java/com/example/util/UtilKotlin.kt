package com.example.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import com.example.controllersystemapp.R
import com.example.util.ApiConfiguration.ErrorBodyResponse
import com.example.util.PrefsUtil.getSharedPrefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.loader_layout.*
import okhttp3.ResponseBody
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
            Log.e("ResponseForError", "${errorResponse?.msg?.get(i)}")

            errortext = errortext + errorResponse?.msg?.get(i) + "\n"

        }
        // var error =errorResponse?.msg?.toString()
        //  error = error?.substring(1,error.length- 1)
        //var error= errorResponse?.message
        return errortext?:"" /*errorResponse?.msg.toString().apply{
      replace("[\\[\\](){}]","")/*replace("[","")
          .replace("]","")*/}*/
    }

}