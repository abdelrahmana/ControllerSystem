package com.example.controllersystemapp.admin.settings.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantModel
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.admin_item.view.*
import kotlinx.android.synthetic.main.delegate_item.view.*

class AdminAdapter(var adminList: ArrayList<Any>,
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


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            accountantModel: Any,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

//            itemView.delegateName.text = accountantModel.name
//            itemView.delegatePhone.text = accountantModel.phone
           // Glide.with(itemView.context!!).load(accountantModel.image).into(itemView.delegateImg)


            itemView.optionIcon?.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}