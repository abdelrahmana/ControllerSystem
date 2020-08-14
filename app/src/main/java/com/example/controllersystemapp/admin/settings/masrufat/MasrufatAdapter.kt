package com.example.controllersystemapp.admin.settings.masrufat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantModel
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.delegate_item.view.*
import kotlinx.android.synthetic.main.masrufat_item.view.*

class MasrufatAdapter(
    var arrayList: ArrayList<Any>,
    var onRecyclerItemClickListener: ClickAcceptRejectListener) :
    RecyclerView.Adapter<MasrufatAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.masrufat_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(arrayList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            accountantModel: Any,
            onRecyclerItemClickListener: ClickAcceptRejectListener
        ) {

//            itemView.delegateName.text = accountantModel.name
//            itemView.delegatePhone.text = accountantModel.phone
           // Glide.with(itemView.context!!).load(accountantModel.image).into(itemView.delegateImg)


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemListClick(adapterPosition)

            }

            itemView.acceptFeesBtn.setOnClickListener {

                onRecyclerItemClickListener.onAcceptClick(adapterPosition)

            }
            itemView.rejectFeesBtn.setOnClickListener {

                onRecyclerItemClickListener.onRejectClick(adapterPosition)

            }





        }


    }


}