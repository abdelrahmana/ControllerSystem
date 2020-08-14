package com.example.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.util.PrefsUtil.getSharedPrefs
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


    fun changeFragmentBack(activity: FragmentActivity, fragment: Fragment, tag: String ,bundle: Bundle?) {

        val transaction = activity?.supportFragmentManager.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        transaction.replace(R.id.frameLayout_direction, fragment, tag)
        //transaction.addToBackStack(tag)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}