package com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.model.DataBean
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.admin_delegate_item.view.*

class AdminDelegatesAdapter(
    var context: Context,
    var delegateList: ArrayList<DataBean>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<AdminDelegatesAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.admin_delegate_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return delegateList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(delegateList[position], onRecyclerItemClickListener)


    }
    fun updateData(newList: List<DataBean>?) {
        val start = itemCount
        if (newList != null) {
            //  if (!newList.isEmpty()) { // if this is the first time
            if (delegateList.size > 0) // has old data
            {
                this.delegateList.addAll(newList)
                notifyItemRangeInserted(start, delegateList.size - 1)
            } else {
                this.delegateList.addAll(newList)
                notifyDataSetChanged()
            }

            //   }
        }
    }

   inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            delegatesModel: DataBean,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.adminDelegateName.text = delegatesModel.name
           itemView.adminDelegatePhone.text = delegatesModel.phone
            Glide.with(itemView.context!!).load(delegatesModel.image?:"").placeholder(R.drawable.image_delivery_item).dontAnimate().into(itemView.adminDelegateImg)


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }






        }


    }


}