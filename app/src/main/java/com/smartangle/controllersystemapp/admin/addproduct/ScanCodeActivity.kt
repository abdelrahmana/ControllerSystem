package com.smartangle.controllersystemapp.admin.addproduct

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smartangle.controllersystemapp.R
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class ScanCodeActivity : AppCompatActivity() , ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    lateinit var model: ViewModelHandleChangeFragmentclass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_code)
        model = UtilKotlin.declarViewModel(this)!!
        mScannerView = ZXingScannerView(this) // Programmatically initialize the scanner view
        setContentView(mScannerView) // Set the scanner view as the content view
    }



    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera() // Start camera on resume
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera() // Stop camera on pause
    }

    override fun handleResult(rawResult: Result?) {
        // Do something with the result here
        Log.d("testttt" , "handle")

        Log.v("FragmentActivity.TAG", rawResult?.text) // Prints scan results
        Log.v("FragmentActivity.TAG", rawResult?.getBarcodeFormat().toString()
        ) // Prints the scan format (qrcode, pdf417 etc.)

       // BARCODE = rawResult?.text?:""
       // model.setStringVar(rawResult?.text?:"")
        val returnIntent = Intent()
        returnIntent.putExtra(SCANERESULT, rawResult?.text?:"")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
       // finish()
        // If you would like to resume scanning, call this method below:
        //mScannerView!!.resumeCameraPreview(this)

    }
    companion object {
        val RES_CODE_B = 1
        val REQUEST_BARCODE = "barCode"
        val scanCode = 340
        val SCANERESULT = "scanResult"

    }

}