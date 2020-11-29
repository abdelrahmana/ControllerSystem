package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerIClickAcceptRejectListener
import com.example.controllersystemapp.admin.interfaces.OnStoreSelcteClickListener
import com.example.controllersystemapp.delegates.makeorder.model.Data
import com.example.controllersystemapp.delegates.notificationreports.models.DelegateNotificationresponse
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.delegate_notification_item.view.*


class DelegateNotificationReportsAdapter(
    val modelData: ViewModelHandleChangeFragmentclass,
    val delegateNotificationlist: ArrayList<DelegateNotificationresponse>,
    val onRecyclerIClickAcceptRejectListener: OnRecyclerIClickAcceptRejectListener
    //this method is returning the view for each item in the list
    //, val listener: OnStoreSelcteClickListener
) : RecyclerView.Adapter<DelegateNotificationReportsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.delegate_notification_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(delegateNotificationlist[position],/*modelData*/delegateNotificationlist)
        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return delegateNotificationlist.size
    }

    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(
            itemData: DelegateNotificationresponse,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<DelegateNotificationresponse>
        ) {

            // itemView.store_name.text = itemData?.name?:""

//            if (adapterPositionChecked == adapterPosition) {
//                showHideViews(itemView, View.VISIBLE)
//                itemView.openCloseImage.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        itemView.context,
//                        R.drawable.ic_btn_close_info
//                    )
//                )
//            } else {
//                showHideViews(itemView, View.GONE)
//                itemView.openCloseImage.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        itemView.context,
//                        R.drawable.ic_btn_open_info
//                    )
//                )
//
//            }
//

            itemView.setOnClickListener {
                onItemClicked(modelData, adapterPosition) // go to details please
            }

            itemView.delegateAcceptNotificationBtn?.setOnClickListener {
                onRecyclerIClickAcceptRejectListener.onItemAcceptClick(adapterPosition)
            }
            itemView.delegateRejectNotificationBtn?.setOnClickListener {
                onRecyclerIClickAcceptRejectListener.onItemAcceptClick(adapterPosition)
            }


        }

        private fun showHideViews(itemView: View, showGone: Int) {
            Log.d("pos", "showw")
            itemView?.addressNotificationContainer?.visibility = showGone
        }


        private fun onItemClicked(model: ViewModelHandleChangeFragmentclass, position: Int) {
            Log.d("pos", "$position")
            delegateNotificationlist[position].isChecked = !delegateNotificationlist[position].isChecked
//            adapterPositionChecked = adapterPosition
//            // model.setNotifyItemSelected(delegateNotificationlist.get(position)?:"") // update sign up fragment please
//            notifyDataSetChanged() // reload this please
            if (delegateNotificationlist[position].isChecked)
            {
                itemView.openCloseImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_btn_close_info
                    )
                )
                showHideViews(itemView,View.VISIBLE)

            }
            else{
                itemView.openCloseImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_btn_open_info
                    )
                )
                showHideViews(itemView,View.GONE)

            }
        }

    }

    var adapterPositionChecked = -1 // this is the checked item to use

}