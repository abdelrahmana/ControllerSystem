package com.smartangle.controllersystemapp.common.delivery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.admin_order_list.view.*

class AdminOrderItemsAdapter(
    var orderItemsList: ArrayList<DataAdminOrderItems>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<AdminOrderItemsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //R.layout.curretn_wallet_item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_order_list , parent , false)
        return ViewHolder(view)
    }

    fun removeItemFromList(position: Int) {
        orderItemsList?.let {
            orderItemsList?.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return orderItemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(orderItemsList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            orderItemData: DataAdminOrderItems,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

           // itemView.walletCategoryTxt?.text = orderItemData?.product?.category?.name?:""
            itemView.newWalletName.text = orderItemData?.product?.name?:""
            itemView.newOrderPrice.text = orderItemData?.price?:""
            itemView.newOrderCurrency.text = orderItemData?.product?.currency?:""
            itemView.newWalletDesc.text = orderItemData?.product?.description?:""
            itemView.newOrderQuantity.text = orderItemData?.quantity?:""


//            itemView.currentWalletName.text = orderWallet?.name?:""
//            itemView.currentWalletDesc.text = orderWallet?.description?:""
//            itemView.price.text = orderWallet?.price?:""
//            itemView.currancy.text = orderWallet?.currency?:""
//            itemView.quantity.text = orderWallet?.total_quantity?:""
//            if (orderWallet?.category == null || orderWallet?.ware_houses.isNullOrEmpty())
//                itemView.slash?.visibility = View.GONE
//            itemView.walletCategoryTxt?.text = orderWallet?.category?.name?:""
//            if (orderWallet?.ware_houses.isNullOrEmpty() == false)
//            {
//                itemView.walletStoreTxt?.text = orderWallet?.ware_houses?.get(0)?.name?:""
//            }
//            itemView.statusCurrentWallet?.visibility = View.GONE






//            Glide.with(itemView.context).load(productsModel.image?:"")
////                .placeholder(R.drawable.no_profile)
////                .error(R.drawable.no_profile)
//                .into(itemView.productImg)

            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}