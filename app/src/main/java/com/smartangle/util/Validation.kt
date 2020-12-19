package com.smartangle.util

import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import java.util.regex.Pattern

class Validation {






companion object{

    private const val YES = true
    private const val NO = false
    private const val EMAIL_PATTERN_1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private const val EMAIL_PATTERN_2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+"


//    private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
//    private const val EMAIL_PATTERN_2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+"


//    private const val EMAIL_PATTERN =
//        "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"
   // private val pattern: Pattern = Pattern.compile(EMAIL_PATTERN)
    //private var matcher: Matcher? = null


    fun isEmailValid(email : String) : Boolean
    {
//        matcher = pattern.matcher(email)
//        return matcher!!.matches()
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$"
        val pattern =
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }


    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun validGlobalPhoneNumber(mobile: String) :Boolean { // should all number starts with +
        return mobile.isNotEmpty()&&mobile.startsWith("+")&&mobile.length>4
    }


    fun validateEmail(editText: EditText): Boolean {
        return if (nonEmpty(editText)) {
            val emailAsString =
                removeBlankSpace(
                    editText.text.toString()
                )
            (emailAsString.matches(EMAIL_PATTERN_1.toRegex())
                    || emailAsString.matches(EMAIL_PATTERN_2.toRegex()))
        } else {
            Log.d("SERI_PAR->Error", "edit text object is null")
            NO
        }
    }

    fun removeBlankSpace(value: String): String {
        var value = value
        value = value.replace(" ", "")
        return value
    }

    fun nonEmpty(editText: EditText?): Boolean {
        return if (editText != null && !TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })) {
            YES
        } else {
            Log.d("SERI_PAR->Error", "edit text object is null")
            NO
        }
    }



    fun isPhoneValid(phoneNumber : String) : Boolean
    {
        Log.d("length" , "${phoneNumber.length}")
        if (phoneNumber.length == 8)
        {
            return phoneNumber.startsWith("5") || phoneNumber.startsWith("6") || phoneNumber.startsWith("9")
        }
        else{
            return false
        }
    }



}

}