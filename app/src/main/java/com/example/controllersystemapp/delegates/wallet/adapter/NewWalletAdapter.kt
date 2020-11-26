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
import kotlinx.android.synthetic.main.new_wallet_item.view.*


class NewWalletAdapter(
    var context: Context,
    var currentWalletList: ArrayList<Data>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<NewWalletAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.new_wallet_item , parent , false)
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
            orderWallet: Data,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.newWalletName.text = " أمر تجهيز رقم ${orderWallet?.order_number}"
            itemView.newOrderPrice.text = orderWallet?.total_price?:""
            itemView.newOrderCurrency.text = orderWallet?.currency?:""
            // itemView.walletStoreTxt?.text = orderWallet?.currency?:""
            if (orderWallet?.details?.isNullOrEmpty() == false)
            {
                itemView.newWalletDesc.text = orderWallet?.details?.get(0)?.product?.name?:""
                itemView.newOrderQuantity.text = (orderWallet?.details?.get(0)?.quantity?:0).toString()?:""
                itemView.newwalletCategoryTxt?.text = orderWallet?.details?.get(0)?.product?.category?.name?:""
            }
            itemView.newSlash?.visibility = View.GONE
            itemView.newwalletStoreTxt?.visibility = View.GONE
           // itemView.walletCategoryTxt?.visibility = View.GONE


//            Glide.with(itemView.context).load(productsModel.image?:"")
////                .placeholder(R.drawable.no_profile)
////                .error(R.drawable.no_profile)
//                .into(itemView.productImg)

            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }

            itemView?.acceptOrderBtn?.setOnClickListener {

                onRecyclerItemClickListener.acceptOrderClickListener(adapterPosition)
            }





        }


    }


}