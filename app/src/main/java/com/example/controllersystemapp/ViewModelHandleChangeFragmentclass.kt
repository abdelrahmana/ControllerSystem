package com.example.controllersystemapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// this viewpager is used within fragment
// specifically when running new fragment and notifying fragment with this change
class ViewModelHandleChangeFragmentclass  : ViewModel() {
    // this basically used to notify changefragment with new
    var notifyChangeFragment = MutableLiveData<Int>()
    var errorMessage = MutableLiveData<String>()

    // handle the back of fragments within activty
    var handleClickBack = MutableLiveData<Boolean>()
    // this is very important to use to handle when result is coming to application
    // while the fragment is destroyed to handle errors of null
    // so this item if true the api is success if false api on error
    var onFragmentChangesHandleResult = MutableLiveData<HandleResultsOfApiModel>()
    // open the loader or shimmer animation while data is coming between parent fragment and child and close it if it false
    var loaderOrShimmerStatus = MutableLiveData<Boolean>()
    var responseDataCode = MutableLiveData<Any>() // lets make this generic to use it with all apis
    var loginDeviceId = MutableLiveData<String>()

    var stringNameData = MutableLiveData<ModelStringID>()
    fun setStringData(modelStringID: ModelStringID?) {
        stringNameData.value = modelStringID
    }

    var intIdData = MutableLiveData<Int>()
    fun setStringData(intData: Int?) {
        intIdData.value = intData
    }



    var productDetailsId = MutableLiveData<Int>()
    fun setProductDetailsId(productDetails: Int?) {
        productDetailsId.value = productDetails
    }


    var loadPreviousNavBottom = MutableLiveData<HandleBottomNavBack>()
    fun responseCodeDataSetter(responseBody : Any?) { // lets post this to our listener places

        this.responseDataCode.postValue(responseBody)
    }
    fun setPreviousNavBottom (handleId:HandleBottomNavBack) // id of what item need to change
    {
        this.loadPreviousNavBottom.value = handleId

    }
    fun setOnFragmentChangesHandleResults(item: HandleResultsOfApiModel?) {
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

    fun onError(onError: String?) {
        errorMessage.postValue(onError)
    }
    fun setFingerPrintDevice(deviceId: String?) {
        loginDeviceId.value = deviceId
    }

    class HandleResultsOfApiModel {
        var isThisSuccess : Boolean? = null
        var errorResult :String ? = null
        var setResult : Any ? = null // this the most generic item to use in  the entire  application
    }

    class HandleBottomNavBack(var id : Int = 0 , var changeSelection : Boolean = false){


    }
}