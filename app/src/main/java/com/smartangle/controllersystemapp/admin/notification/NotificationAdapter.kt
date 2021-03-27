package com.smartangle.controllersystemapp.admin.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.UtilKotlin
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationAdapter(
    var notificationList: ArrayList<Data>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(notificationList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            notification: Data,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.notificationName?.text = notification.text?:""
            itemView.notificationDetails?.text =UtilKotlin.getFormatedDate(notification.created_at?:"",itemView.context!!)

//            itemView.delegateName.text = accountantModel.name
//            itemView.delegatePhone.text = accountantModel.phone
           // Glide.with(itemView.context!!).load(accountantModel.image).into(itemView.delegateImg)


        /*    itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }*/





        }


    }


}