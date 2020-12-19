package com.smartangle.controllersystemapp.admin.specialcustomers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.special_customer_item.view.*

class SpecialCustomerAdapter(
    var accountantsList: ArrayList<ClientsData>,
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
            clientsModel: ClientsData,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.customerName.text = clientsModel.name?:""
            itemView.customerPhone.text = clientsModel.phone?:""
            itemView.receiptPrice.text = clientsModel.receipt_amount?.toString()?:""
            itemView.creditorPrice.text = clientsModel.creditor_amount?.toString()?:""
            itemView.receiptCurrancy.text = clientsModel.currency?:""
            itemView.creditorCurrancy.text = clientsModel.currency?:""
            itemView.currancyCustomer.text = clientsModel.currency?:""
            itemView.totalPriceCustomer.text = clientsModel.total_amount?.toString()?:""


            //Glide.with(itemView.context!!).load(clientsModel.).into(itemView.customerImg)


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}