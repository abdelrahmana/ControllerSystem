package com.smartangle.controllersystemapp.accountant.sales.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.sales.model.Data
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_not_received_sales_item.view.*
import kotlinx.android.synthetic.main.fragment_received_sales_item.view.*


class ReceivedSalesAdapter(
    var context: Context,
    var notReceivedSalesList: ArrayList<Data>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<ReceivedSalesAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_received_sales_item , parent , false)
        return ViewHolder(
            view
        )
    }

//    fun removeItemFromList(position: Int) {
//        productList?.let {
//            productList?.removeAt(position)
//            notifyDataSetChanged()
//        }
//    }

    override fun getItemCount(): Int {
        return notReceivedSalesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(notReceivedSalesList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            datamodel: Data,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

//            Glide.with(itemView.context).load(productsModel.image?:"")
////                .placeholder(R.drawable.no_profile)
////                .error(R.drawable.no_profile)
//                .into(itemView.productImg)
//            itemView.productName.text = productsModel.name
//            itemView.productDesc.text = productsModel.description
//            itemView.price.text = productsModel.price
//            itemView.quantity.text = productsModel.total_quantity
//            itemView.currancy.text = productsModel.currency

            itemView.notReceivedSalesName?.text = itemView.notReceivedSalesName?.text.toString() + datamodel.order_number?:""

            itemView.notReceivedDelegate?.text = datamodel?.name?:""
            itemView.notReceivedPrice?.text = datamodel?.total_price?:""
            itemView.notReceivedSalesQuantity?.text = datamodel?.details?.get(0)?.product?.total_quantity?:""
            itemView.notReceivedDateTxt?.text = UtilKotlin.getFormatedDate(datamodel?.created_at?:"0",itemView.context)
            itemView.notReceivedCurrency?.text = datamodel?.currency?:""

            itemView.containerRecieved.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }







        }


    }


}