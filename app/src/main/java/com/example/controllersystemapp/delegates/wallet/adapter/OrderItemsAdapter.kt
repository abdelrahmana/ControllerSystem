package com.example.controllersystemapp.delegates.wallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.delegates.wallet.models.Data
import com.example.controllersystemapp.delegates.wallet.models.DataDelegateOrderItems
//import com.example.controllersystemapp.delegates.wallet.models.ItemsData
import kotlinx.android.synthetic.main.curretn_wallet_item.view.*


class OrderItemsAdapter(
    var context: Context,
    var currentWalletList: ArrayList<DataDelegateOrderItems>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<OrderItemsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.curretn_wallet_item , parent , false)
        return ViewHolder(view)
    }

    fun removeItemFromList(position: Int) {
        currentWalletList?.let {
            currentWalletList?.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return currentWalletList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(currentWalletList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            orderWallet: DataDelegateOrderItems,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

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


            itemView.currentWalletName.text = orderWallet?.product?.name?:""
            //itemView.currentWalletDesc.text = orderWallet?.description?:""
            itemView.price.text = orderWallet?.price?:""
            itemView.currancy.text = orderWallet?.product?.currency?:""
            itemView.quantity.text = orderWallet?.quantity?:""
            itemView.slash?.visibility = View.GONE
//            if (orderWallet?.category == null || orderWallet?.ware_houses.isNullOrEmpty())
//                itemView.slash?.visibility = View.GONE
//            itemView.walletCategoryTxt?.text = orderWallet?.category?.name?:""
//            if (orderWallet?.ware_houses.isNullOrEmpty() == false)
//            {
//                itemView.walletStoreTxt?.text = orderWallet?.ware_houses?.get(0)?.name?:""
//            }
            itemView.statusCurrentWallet?.visibility = View.GONE




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