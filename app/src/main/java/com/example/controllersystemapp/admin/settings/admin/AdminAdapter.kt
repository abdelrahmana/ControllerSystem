package com.example.controllersystemapp.admin.settings.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.admin_item.view.*

class AdminAdapter(var adminList: ArrayList<Admin>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<AdminAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_item , parent , false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(adminList[position], onRecyclerItemClickListener)


    }


    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            adminModel: Admin,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.adminNameList.text = adminModel.name
            itemView.delegatePhone.text = adminModel.phone
            Glide.with(itemView.context!!).load(adminModel.image).into(itemView.delegateImg)


            itemView.optionIcon?.setOnClickListener {

                onRecyclerItemClickListener.adminOptionsClickListener(adapterPosition)

            }

            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}