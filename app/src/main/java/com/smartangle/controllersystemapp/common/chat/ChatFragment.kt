package com.smartangle.controllersystemapp.common.chat

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.DelegatesFragment
import com.smartangle.controllersystemapp.common.AuthPresenter
import com.smartangle.controllersystemapp.common.chat.model.Receiver
import com.smartangle.controllersystemapp.common.chat.model.RequestMessgae
import com.smartangle.controllersystemapp.common.chat.model.ResponseChatList
import com.smartangle.controllersystemapp.common.chat.model.Sender
import com.smartangle.controllersystemapp.common.login.LoginRequest
import com.smartangle.controllersystemapp.common.login.LoginResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.PrefsUtil
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Response

class ChatFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    inner class CustomReceiver(/*var viewModel  : ViewModelHandleChangeFragmentclass*/) : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            // Toast.makeText(activity,"onRecieve comes  ", Toast.LENGTH_LONG).show()

          //  val userId: Int = intent.getIntExtra(NameUtils.user_id,0)?:0
           // if (infoOfReciever?.id==userId)
            infoOfReciever=UtilKotlin.getDelegateCallCenter(intent.getStringExtra(NameUtils.other_info)?:"")
            getchatList(false)
            // onMessageEvent(MessageEvent(lat,long))
        }
    }
    var chatAdapter : ChatAdapter?=null
    var receiver : CustomReceiver?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        webService = ApiManagerDefault(context!!).apiService
        // Inflate the layout for this fragment
        receiver = CustomReceiver()
        activity!!.registerReceiver(receiver,IntentFilter(MY_TRIGGER))
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    var infoOfReciever : CallCenterDelegateData?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         infoOfReciever=UtilKotlin.getDelegateCallCenter(arguments?.getString(DelegatesFragment.SELECTEDDELEGATE)?:"")
        // add user info
        recieverName?.text = infoOfReciever?.name?:""
        Glide.with(context!!).load(infoOfReciever?.image?:"").dontAnimate().placeholder(R.drawable.ic_username).into(recieverImage)
        setInitialInfoChatAdapter()
        getchatList()
    sendButton?.setOnClickListener{
        if (UtilKotlin.checkAvalibalityOptions(messageEditText.text.toString())==true)
            postChat()
    }

    }

    var arrayList = ArrayList<com.smartangle.controllersystemapp.common.chat.model.Message>()
    private fun setInitialInfoChatAdapter() {
        chatAdapter = ChatAdapter(arrayList)
        UtilKotlin.setRecycleView(chatList, chatAdapter!!,
            RecyclerView.VERTICAL,context!!, null, true)

    }
    var progressDialog: Dialog?=null
    var webService : WebService?=null
    private fun getchatList(showProgressDialog : Boolean =true) {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            if (showProgressDialog)
            progressDialog?.show()
            ChatPresenter.listMessages(webService!! , infoOfReciever?.id?:0, getChatObserver())
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    private fun postChat() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val requestMessgae = RequestMessgae(messageEditText.text.toString(),infoOfReciever?.id?:0)
            ChatPresenter.sendNewMessagePost(webService!! , requestMessgae, postChatObserver())
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    override fun onDestroyView() {
        activity?.unregisterReceiver(receiver)
        getChatObserver().dispose()
        postChatObserver().dispose()
        super.onDestroyView()
    }
    private fun postChatObserver(): DisposableObserver<Response<SuccessModel>> {

        return object : DisposableObserver<Response<SuccessModel>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<SuccessModel>) {
                progressDialog?.dismiss()

                if (response!!.isSuccessful) {
                    buildMeAsMessageSender(messageEditText?.text.toString())
                }
                else
                {
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
    }
    private fun getChatObserver(): DisposableObserver<Response<ResponseChatList>> {

        return object : DisposableObserver<Response<ResponseChatList>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<ResponseChatList>) {
                progressDialog?.dismiss()

                if (response!!.isSuccessful) {
                    chatAdapter?.arrayList?.clear()
                    chatAdapter?.updateData(response.body()?.data?.messages?:ArrayList())
                    chatList?.smoothScrollToPosition(((chatAdapter?.arrayList?.size)?:1)-1)

                }
                else
                {
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
    }
    fun buildMeAsMessageSender(m: String){ // then add to my recycleview
         // build model before update recycle view with new message
        var message = com.smartangle.controllersystemapp.common.chat.model.Message("",PrefsUtil.getUserModel(context!!)?.id?:0,m,
            Receiver(infoOfReciever?.id?:0,infoOfReciever?.image?:"",infoOfReciever?.name?:"",""),(infoOfReciever?.id?:0).toString(),
            Sender(PrefsUtil.getUserModel(context!!)?.id?:0,PrefsUtil.getUserModel(context!!)?.image?:"",
                PrefsUtil.getUserModel(context!!)?.name?:"",""),(PrefsUtil.getUserModel(context!!)?.id?:0).toString(),"sender","")

        chatAdapter?.updateData(ArrayList<com.smartangle.controllersystemapp.common.chat.model.Message>().also{it.add(message)}) // update current list
     messageEditText?.setText("")
        chatList?.smoothScrollToPosition(((chatAdapter?.arrayList?.size)?:1)-1)
    }

companion object {
    val MY_TRIGGER = "trigger"
}
}