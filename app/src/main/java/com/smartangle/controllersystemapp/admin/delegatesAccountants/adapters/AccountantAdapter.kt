package com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantData
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.admin_delegate_item.view.*

class AccountantAdapter(
    var context: Context,
    var accountantsList: ArrayList<AccountantData>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<AccountantAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.admin_delegate_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return accountantsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(accountantsList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            accountantModel: AccountantData,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.adminDelegateName?.text = accountantModel.name
            itemView.adminDelegatePhone?.text = accountantModel.phone
            Glide.with(itemView.context!!).load(accountantModel.image).into(itemView.adminDelegateImg)


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}