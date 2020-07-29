package com.example.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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
    fun getLocalLanguage(context: Context): String? {
        val locale: Locale
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
        return locale.language
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
}