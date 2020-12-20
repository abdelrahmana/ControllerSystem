package com.smartangle.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


// this activity to work with all other activities
open class CommonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        UtilKotlin.setLanguagePerActivity(
            this,
            null
        ) // before set content view
        super.onCreate(savedInstanceState)
    }

     override fun onResume() {
         UtilKotlin.setLanguagePerActivity(
             this,
             null
         )
       //  UtilKotlin.setLocalLanguage(UtilKotlin.getSharedPrefs(this).getString(PrefsModel.localLanguage,"ar"))
         super.onResume()
     }


     companion object {

         }


 }
