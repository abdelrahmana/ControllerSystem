package com.example.controllersystemapp.admin.delegatesAccountants.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener

class AdminDelegatesAdapter(
    var context: Context,
    var delegateList: ArrayList<Any>,
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

   inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            delegatesModel: Any,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

//            itemView.delegateName.text = delegatesModel.name
//            itemView.delegatePhone.text = delegatesModel.phone
//            Glide.with(itemView.context!!).load(delegatesModel.image?:"").placeholder(R.drawable.image_delivery_item).dontAnimate().into(itemView.delegateImg)


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }






        }


    }


}