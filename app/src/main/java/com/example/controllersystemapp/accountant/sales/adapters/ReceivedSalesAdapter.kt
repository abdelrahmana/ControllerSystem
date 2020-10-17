package com.example.controllersystemapp.accountant.sales.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_received_sales_item.view.*


class ReceivedSalesAdapter(
    var context: Context,
    var notReceivedSalesList: ArrayList<Any>,
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
            productsModel: Any,
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



            itemView.sendAdminBtn.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }







        }


    }


}