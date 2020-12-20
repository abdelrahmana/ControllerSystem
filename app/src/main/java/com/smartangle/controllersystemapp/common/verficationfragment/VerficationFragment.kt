package com.smartangle.controllersystemapp.common.verficationfragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.smartangle.controllersystemapp.R
import com.smartangle.util.PrefsUtil
import com.smartangle.util.UtilKotlin
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.verfication_fragment.*
import java.util.concurrent.TimeUnit


class VerficationFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.verfication_fragment, container, false)
    }
    var phoneNumber: String = ""
    var phoneCode = ""
    var cTimer: CountDownTimer? = null // android timer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //phoneNumber = arguments!!.getString(phoneNumberKey)?:""
        // phoneCode  = arguments!!.getString(phoneCodeKey)?:""
        UtilKotlin.setUnderLine(getString(R.string.send_ramz),sendAgain)
        backButton?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        phoneNumber = PrefsUtil.getSharedPrefs(context!!).getString(phoneNumberKey, "")?:""
        //   phoneCode = PrefUtils.getString(getContext(), phoneCodeKey, "")
        //   var email = PrefUtils.getString(context, "email", "")
        setOtpOnFinishListener()
        try {
            // if (phoneCode == "+20" && phoneNumber.startsWith("0")) // in case of starting with 0 and phone code egypt
            //       phoneNumber = phoneNumber.substring(1)
            // numberTextView.append(subStringDigits)
            //    filterForRequest = intent.getStringExtra(filter)
            backButton?.setOnClickListener{
                // this should preform action clicked
                isDestroyedBeforeSend = true // to handle requests
                cTimer!!.cancel()
                activity!!.finish()

            }
            confirmButton?.setOnClickListener {
                // Validate button
                if (otp_view.text?.isNotEmpty()==true)
                    verifyVerificationCode(/*phoneNumber*/otp_view.text?.trim().toString())
                else
                    UtilKotlin.showSnackErrorInto(activity,getString(R.string.code))
                //    validation_info.append(email +" "+context!!.getString(R.string.validation_numb)+" "+phoneCode+phoneNumber +" "+context!!.getString(R.string.sms_validation))
            }
            // lets get the intent phone number we have sent
            sendVerificationCode(phoneNumber)
        } catch (e: Exception) {

        }

        not_recieved.setOnClickListener {
            sendVerificationCode(phoneNumber)

            not_recieved.visibility = View.GONE
            sendAgain.visibility = View.GONE
            timerTextView.visibility = View.VISIBLE
        }
    }

    private fun setOtpOnFinishListener() {
        otp_view.setOtpCompletionListener({ otp -> // do Stuff
            verifyVerificationCode(otp)
        })
    }

    private fun sendVerificationCode(no: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,  // add the number with 012/010/011/015
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks
        )
        countDownTimer()
    }

    var mVerificationId: String? = ""

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationFailed(p0: com.google.firebase.FirebaseException) {
            try {

                UtilKotlin.showSnackErrorInto(activity, p0.message.toString())

                //  customDialogBuilder(/*p0!!.message.toString() +*/ "لم نتمكن من إرسال الرمز حاليا", true)
            } catch (e: Exception) {

            }

        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            try {
                if (isDestroyedBeforeSend)
                    return
                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {

                    otp_view.setText(code)
                    //verifying the code
                  //  verifyVerificationCode(code)
                } else {
                    verifyVerificationWhenInstantVerfication(phoneAuthCredential)

                }
            } catch (e: Exception) {

            }

        }

        override fun onCodeSent(
            s: String,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(s, forceResendingToken)

            //storing the verification id that is sent to the user
            mVerificationId = s
        }
    }

    private fun verifyVerificationCode(code: String?) {
        //creating the credential
        try {


            val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code!!)

            //signing the user
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
            // customDialogBuilder("رمز التحقق الذى ادخلته غير صحيح",true)
        }
    }

    override fun onDestroyView() {
        isDestroyedBeforeSend = true // to handle requests
        if (cTimer !=null)
            cTimer!!.cancel()
        super.onDestroyView()

    }

    private fun verifyVerificationWhenInstantVerfication(phoneAuthCredential: PhoneAuthCredential) {
        //creating the credential
        try {


            //signing the user
            signInWithPhoneAuthCredential(phoneAuthCredential)
        } catch (e: Exception) {
            //     customDialogBuilder("رمز التحقق الذى ادخلته غير صحيح",true)
        }
    }

    private fun hideSoftKey() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        try {

            if (imm.isAcceptingText())
                imm.hideSoftInputFromWindow(activity!!.currentFocus!!.getWindowToken(), 0)
        } catch (e: Exception) {

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                hideSoftKey()
                if (task.isSuccessful()) { // login is completed Successfully
                    /*  if (filterForRequest.equals(signIn))
                          startHomeActivityNow(phoneNumber)
                      else if (filterForRequest.equals(renewPhone)){
                          setResultData()

                      } else if (filterForRequest.equals(signUp)){
                          setResultData()
                      }*/
                    setResultData()

                } else {

                    //verification unsuccessful.. display an error message

                    var message =/* task.getException().toString()+*/
                        " حدث خطأ أثناء الدخول حاول مرة أخرى .برجاء التأكد من وجود الشريحة فى الهاتف"

                    if (task.getException() is FirebaseAuthInvalidCredentialsException) {
                        message = /*task.exception!!.message.toString() + */
                            "الرمز الذى أدخلته غير صحيح برجاء إدخال الرمز الصحيح وإعادة المحاولة "
                    }
                    UtilKotlin.showSnackErrorInto(activity, message)

                    // customDialogBuilder(message,true) // this is error
                    //   btnConfirm.isEnabled = true

                }
            }
    }

    private fun setResultData() {
        val data = Intent()
        cancelTimer()
        //  val  videoPath  = file!!.absolutePath // the path of the file in storage
        //  data.putExtra("video", videoPath) // this paths is from storage to what inside
        activity!!.setResult(Activity.RESULT_OK, data)
        activity!!.finish() // this
    }


    var dialog: Dialog? = null


    fun countDownTimer() {
        // try to cancel the previouis one
        cancelTimer()
        cTimer = object : CountDownTimer(60000, 1000) { // for 15 seconds
            override fun onTick(millisUntilFinished: Long) {

                timerTextView.text =
                    "( " + convertSecondsToHMmSs((millisUntilFinished / 1000).toLong()) + " )"
            }

            override fun onFinish() {

                timerTextView.visibility = View.GONE
                not_recieved.visibility = View.VISIBLE
                sendAgain.visibility = View.VISIBLE
            }
        }
        cTimer!!.start()
    }

    fun cancelTimer() {
        if (cTimer != null)
            cTimer!!.cancel()
    }

    fun convertSecondsToHMmSs(seconds: Long): String? {
        val s = seconds % 60
        val m = seconds / 60 % 60
        //val h = seconds / (60 * 60) % 24
        return String.format("%02d:%02d", m, s) ?: "0:0" //%d: // to hours
    }
    var isDestroyedBeforeSend = false

    companion object {
        val phoneNumberKey = "phone_number"
        /*   @kotlin.jvm.JvmField
           val phoneCodeKey = "phone_code"
           @kotlin.jvm.JvmField
           val requestValdiationPhoneNumber = 101*/
    }


}