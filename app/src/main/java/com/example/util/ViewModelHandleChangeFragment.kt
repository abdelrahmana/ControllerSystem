package com.example.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.controllersystemapp.ModelStringID
import okhttp3.ResponseBody

// this viewpager is used within fragment
// specifically when running new fragment and notifying fragment with this change
class ViewModelHandleChangeFragmentclass  : ViewModel() {
    // this basically used to notify changefragment with new
    var notifyChangeFragment = MutableLiveData<Int>()
    var errorMessage = MutableLiveData<ResponseBody>()

    // handle the back of fragments within activty
    var handleClickBack = MutableLiveData<Boolean>()
    // this is very important to use to handle when result is coming to application
    // while the fragment is destroyed to handle errors of null
    // so this item if true the api is success if false api on error
    var onFragmentChangesHandleResult = MutableLiveData<HandleResultsOfApiModel>()
    // open the loader or shimmer animation while data is coming between parent fragment and child and close it if it false
    var loaderOrShimmerStatus = MutableLiveData<Boolean>()

    var loadPreviousNavBottom = MutableLiveData<Int>()
    var screenShotOfActivity = MutableLiveData<Any>() //bitmap

    var showLoader = MutableLiveData<Boolean>()
    fun setShowLoader(boolean: Boolean?)
    {
        showLoader.postValue(boolean)
    }
    var stringNameData = MutableLiveData<ModelStringID>()

    var stringDataVar = MutableLiveData<String>() // lets make this for sharing data overall application
    var InteDataVariable = MutableLiveData<Int>() // lets make this for sharing data overall application
    var updateOrDelete = MutableLiveData<String>() // lets make this for sharing data overall application

    var responseDataCode = MutableLiveData<Any>() // lets make this generic to use it with all apis
    var notifyItemSelected = MutableLiveData<Any>() // lets make this for sharing data overall application

    fun setStringData(modelStringID: ModelStringID?) {
        this.stringNameData.value = modelStringID
    }

    fun setStringVar(stringDataVar: String?) {
        this.stringDataVar.postValue(stringDataVar)
    }

    fun setUpdateOrDelete(stringDataVar: String?) {
        this.updateOrDelete.postValue(stringDataVar)
    }

    var intIdData = MutableLiveData<Int>()
//    fun setStringData(intData: Int?) {
//        intIdData.value = intData
//    }

    var productDetailsId = MutableLiveData<Int>()
    fun setProductDetailsId(productDetails: Int?) {
        productDetailsId.value = productDetails
    }

    fun setInteDataVariable(intIdData : Int?) { // lets post this to our listener places

        this.intIdData.value = intIdData
    }

    fun onError(onError: ResponseBody?) {
        errorMessage.postValue(onError)
    }
    fun setNotifyItemSelected(responseBody : Any?) { // lets post this to our listener places

        this.notifyItemSelected.value =responseBody
    }
    fun setScreenShot (screentShot:Any) // id of what item need to change
    {
        this.screenShotOfActivity.value = screentShot

    }
    fun setPreviousNavBottom (id:Int) // id of what item need to change
    {
        this.loadPreviousNavBottom.value = id

    }
    fun setOnFragmentChangesHandleResults(item:HandleResultsOfApiModel?) {
        this.onFragmentChangesHandleResult.value = item
    }
    fun setHandleClickBack(item:Boolean?) {
        this.handleClickBack.value = item
    }
    // if it false then child requests calls from api
    fun setloaderShimmerAnimation(item:Boolean?) { // the child fragments set it to false to close the loader when the data is already setted in views
        this.loaderOrShimmerStatus.value = item
    }
    fun GetnotifyChangeFragment(): Int? {
        return notifyChangeFragment.value
    }

    fun setNotifyChange(item:Int?) {
        this.notifyChangeFragment.value = item
    }
     class HandleResultsOfApiModel {
        var isThisSuccess : Boolean? = null
        var errorResult :String ? = null
        var setResult : Any ? = null // this the most generic item to use in  the entire  application
    }
    fun responseCodeDataSetter(responseBody : Any?) { // lets post this to our listener places

        this.responseDataCode.postValue(responseBody)
    }
}