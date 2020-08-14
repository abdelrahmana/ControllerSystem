package com.example.controllersystemapp.admin.delegatesAccountants.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.models.DelegatesModel
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.delegate_item.view.*

class DelegatesAdapter(
    var context: Context,
    var delegateList: ArrayList<DelegatesModel>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
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


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            delegatesModel: DelegatesModel,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.delegateName.text = delegatesModel.name
            itemView.delegatePhone.text = delegatesModel.phone
           // Glide.with(itemView.context!!).load(delegatesModel.image).into(itemView.delegateImg)


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}