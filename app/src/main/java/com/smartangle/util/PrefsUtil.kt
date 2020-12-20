package com.smartangle.util

import android.content.Context
import android.content.SharedPreferences
import androidx.multidex.BuildConfig
import com.smartangle.controllersystemapp.common.login.User
import com.smartangle.util.NameUtils.LANGUAGE
import com.smartangle.util.PrefsModel.TOKEN
import com.smartangle.util.PrefsModel.shareredPrefName
import com.smartangle.util.PrefsModel.userModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

    fun setUserModel(context: Context, responseLogin: User?) {

        val gson = Gson()
        getSharedPrefs(context).edit().putString(userModel, gson.toJson(responseLogin ?: User())
        ).apply()
    }

    fun getUserModel(ctx: Context): User? { // this should return the object
        val jso = getSharedPrefs(ctx).getString(userModel, "") // get the overall object please
        val gson = Gson()
        val typeToken = object : TypeToken<User?>() {}.type
        val obj = gson.fromJson<User>(jso, typeToken) ?: User() //ResponseLogin(Data("", null))
        return obj

    }

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

    fun setString(
        ctx: Context,
        key: String?,
        value: String?
    ) {
//        val sp = ctx.getSharedPreferences(
//            PREF_NAME,
//            Context.MODE_PRIVATE
//        )
        var encrypted = value
        try {
            encrypted = AESUtils.encrypt(value!!)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        getSharedPrefs(ctx).edit().putString(key, value).apply()
    }



}