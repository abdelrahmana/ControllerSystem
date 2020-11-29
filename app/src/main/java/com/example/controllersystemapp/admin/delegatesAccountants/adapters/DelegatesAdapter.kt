package com.example.controllersystemapp.admin.delegatesAccountants.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.delegate_item.view.*

class DelegatesAdapter(
    var context: Context,
    var delegateList: ArrayList<CallCenterDelegateData>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener,var showOptions : Int = View.VISIBLE ) :
    RecyclerView.Adapter<DelegatesAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.delegate_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return delegateList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(delegateList[position], onRecyclerItemClickListener)


    }
    fun updateData(newList: List<CallCenterDelegateData>?) {
        val start = itemCount
        if (newList != null) {
            if (!newList.isEmpty()) { // if this is the first time
                if (delegateList.size > 0) // has old data
                {
                    this.delegateList.addAll(newList)
                    notifyItemRangeInserted(start, delegateList.size - 1)
                } else {
                    this.delegateList.addAll(newList)
                    notifyDataSetChanged()
                }

            }
        }
    }

   inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            delegatesModel: CallCenterDelegateData,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.delegateName.text = delegatesModel.name
            itemView.delegatePhone.text = delegatesModel.phone
            Glide.with(itemView.context!!).load(delegatesModel.image?:"").placeholder(R.drawable.image_delivery_item).dontAnimate().into(itemView.delegateImg)


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }
            itemView.optionIcon.visibility = showOptions
            itemView.optionIcon.setOnClickListener{
                onRecyclerItemClickListener.delegateClickListener(adapterPosition)
            }





        }


    }


}