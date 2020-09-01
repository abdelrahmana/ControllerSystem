package com.example.util

import android.util.Log
import java.util.regex.Pattern

class Validation {






companion object{


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