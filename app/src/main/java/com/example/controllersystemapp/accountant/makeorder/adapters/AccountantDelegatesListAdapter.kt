package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.example.controllersystemapp.admin.interfaces.OnStoreSelcteClickListener
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.responsible_person_item.view.*


class AccountantDelegatesListAdapter(
    val modelData: ViewModelHandleChangeFragmentclass,
    val delegateProductsslist: ArrayList<CallCenterDelegateData>
    //this method is returning the view for each item in the list
    //, val listener: OnStoreSelcteClickListener
) : RecyclerView.Adapter<AccountantDelegatesListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.responsible_person_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(delegateProductsslist[position],/*modelData*/delegateProductsslist)
        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return delegateProductsslist.size
    }

//    fun getSelected(): ArrayList<Data>? {
//        val selected: ArrayList<Data> = ArrayList()
//        for (i in 0 until delegateProductsslist.size) {
//            if (delegateProductsslist.get(i).isChecked!!) {
//                selected.add(delegateProductsslist[i])
//            }
//        }
//        return selected
//    }

    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(
            itemData: CallCenterDelegateData,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<CallCenterDelegateData>
        ) {

            Glide.with(itemView.context).load(itemData.image ?: "")
//                .placeholder(R.drawable.no_profile)
//                .error(R.drawable.no_profile)
                .into(itemView.personImg)

            itemView.personName.text = itemData.name
            itemView.personPhone.text = itemData.phone

            if (adapterPositionChecked == adapterPosition) {
                showHideViews(itemView, View.VISIBLE)
            } else {
                showHideViews(itemView, View.GONE)
            }

            itemView.setOnClickListener {

                onItemClicked(modelData, adapterPosition) // go to details please
            }


        }

        private fun showHideViews(itemView: View, showGone: Int) {
            Log.d("pos", "showw")

            itemView?.doneImg?.visibility = showGone
           // itemView?.containerNumber.visibility = showGone
        }


        private fun onItemClicked(model: ViewModelHandleChangeFragmentclass, position: Int) {
            Log.d("pos", "$position")
            adapterPositionChecked = adapterPosition
            model.setNotifyItemSelected(
                delegateProductsslist.get(position) ?: ""
            ) // update sign up fragment please
            notifyDataSetChanged() // reload this please

        }

    }

    var adapterPositionChecked = -1 // this is the checked item to use

}