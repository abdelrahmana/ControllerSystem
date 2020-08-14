package com.example.controllersystemapp.admin.specialcustomers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantModel
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.delegate_item.view.*

class SpecialCustomerAdapter(
    var accountantsList: ArrayList<Any>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<SpecialCustomerAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.special_customer_item , parent , false)
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
            accountantModel: Any,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

//            itemView.delegateName.text = accountantModel.name
//            itemView.delegatePhone.text = accountantModel.phone
           // Glide.with(itemView.context!!).load(accountantModel.image).into(itemView.delegateImg)


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}