package com.example.util

import android.content.Context
import android.content.SharedPreferences
import com.example.controllersystemapp.BuildConfig
import com.example.util.NameUtils.LANGUAGE
import com.example.util.PrefsModel.TOKEN
import com.example.util.PrefsModel.shareredPrefName

object PrefsUtil {

    fun getUserToken(ctx: Context): String? {
        val sp = ctx.getSharedPreferences(
            shareredPrefName, Context.MODE_PRIVATE)
        var value = sp.getString(TOKEN, "")
        try {
            value = AESUtils.decrypt(value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }

    fun removeKey(ctx: Context, key: String?) {
        val sp = ctx.getSharedPreferences(
            shareredPrefName,
            Context.MODE_PRIVATE
        )
        sp.edit().remove(key).apply()
    }
    fun getSharedPrefs(ctx: Context) : SharedPreferences {
      return ctx.getSharedPreferences(
            shareredPrefName,
            Context.MODE_PRIVATE
        )
    }
    fun setLoginState(ctx: Context, isLoggedIn: Boolean) {
        val sp = ctx.getSharedPreferences(
            shareredPrefName, Context.MODE_PRIVATE)
        sp.edit().putBoolean(PrefsModel.isLoggedIn, isLoggedIn).apply()
    }


    fun setUserToken(ctx: Context, token: String?) {
        val sp = ctx.getSharedPreferences(
            shareredPrefName, Context.MODE_PRIVATE)
        var encrypted = token
        if (BuildConfig.DEBUG) {
            sp.edit().putString("$TOKEN-DEBUG", PrefsModel.BEARER + encrypted).apply()
        }
        try {
            encrypted = AESUtils.encrypt(PrefsModel.BEARER + encrypted!!)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        sp.edit().putString(TOKEN, encrypted).apply()
    }

  /*  fun setUserModel(context: Context, responseLogin: ResponseLogin.Client?) {

        val gson = Gson()
        getSharedPrefs(context).edit().putString(userModel, gson.toJson(responseLogin ?: ResponseLogin.Client())
        ).apply()
    }

    fun getUserModel(ctx: Context): ResponseLogin.Client? { // this should return the object
        val jso = getSharedPrefs(ctx).getString(userModel, "") // get the overall object please
        val gson = Gson()
        val typeToken = object : TypeToken<ResponseLogin.Client?>() {}.type
        val obj = gson.fromJson<ResponseLogin.Client>(jso, typeToken) ?:ResponseLogin.Client() //ResponseLogin(Data("", null))
        return obj

    } */

    fun isLoggedIn(ctx: Context): Boolean {
        val sp = ctx.getSharedPreferences(
            shareredPrefName, Context.MODE_PRIVATE)
        return sp.getBoolean(PrefsModel.isLoggedIn, false)
    }


    fun setLanguage(ctx: Context, value: String?) {
        val sp = ctx.getSharedPreferences(
            shareredPrefName,
            Context.MODE_PRIVATE
        )
        var encrypted = value
        try {
            encrypted = AESUtils.encrypt(value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sp.edit().putString(LANGUAGE, value).apply()
    }

    fun getLanguage(ctx: Context): String? {
        val sp = ctx.getSharedPreferences(
            shareredPrefName,
            Context.MODE_PRIVATE
        )
        var value = sp.getString(LANGUAGE, "en")
        try {
            value = AESUtils.decrypt(value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }
}