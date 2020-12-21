package com.smartangle.controllersystemapp.common.chat

import android.app.Dialog
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
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Response

class ChatFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var chatAdapter : ChatAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        webService = ApiManagerDefault(context!!).apiService
        // Inflate the layout for this fragment
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
        if (UtilKotlin.checkAvalibalityOptions(messageEditText.text)==true)
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
    private fun getchatList() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
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
        getChatObserver().dispose()
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
                    chatAdapter?.updateData(response.body()?.data?.messages?:ArrayList())

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
            Receiver(infoOfReciever?.id?:0,infoOfReciever?.image?:"",infoOfReciever?.name?:"",""),infoOfReciever?.id?:0,
            Sender(PrefsUtil.getUserModel(context!!)?.id?:0,PrefsUtil.getUserModel(context!!)?.image?:"",
                PrefsUtil.getUserModel(context!!)?.name?:"",""),PrefsUtil.getUserModel(context!!)?.id?:0,"sender","")

        chatAdapter?.updateData(ArrayList<com.smartangle.controllersystemapp.common.chat.model.Message>().also{it.add(message)}) // update current list
     messageEditText?.setText("")
    }
}