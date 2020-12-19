package com.smartangle.controllersystemapp.accountant.delegatecallcenter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.AccDelegateOrderItemsData
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
//import com.example.controllersystemapp.delegates.wallet.models.ItemsData
import kotlinx.android.synthetic.main.curretn_wallet_item.view.*


class AccDelegateOrderItemsAdapter(
    var context: Context,
    var orderItemsList: ArrayList<AccDelegateOrderItemsData>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<AccDelegateOrderItemsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.curretn_wallet_item , parent , false)
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
            orderWallet: AccDelegateOrderItemsData,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.currentWalletName.text = orderWallet?.product?.name?:""
            itemView.currentWalletDesc.text = orderWallet?.product?.description?:""
            itemView.price.text = orderWallet?.price?:""
            itemView.currancy.text = orderWallet?.product?.currency?:""
            itemView.quantity.text = orderWallet?.quantity?:""
            itemView.slash?.visibility = View.GONE
            itemView.walletStoreTxt?.visibility = View.GONE
            //|| orderWallet?.ware_houses.isNullOrEmpty()
//            if (orderWallet?.product?.category == null )
//                itemView.slash?.visibility = View.GONE
            itemView.walletCategoryTxt?.text = orderWallet?.product?.category?.name?:""
//            if (orderWallet?.ware_houses.isNullOrEmpty() == false)
//            {
//                itemView.walletStoreTxt?.text = orderWallet?.ware_houses?.get(0)?.name?:""
//            }
            itemView.statusCurrentWallet?.visibility = View.GONE



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