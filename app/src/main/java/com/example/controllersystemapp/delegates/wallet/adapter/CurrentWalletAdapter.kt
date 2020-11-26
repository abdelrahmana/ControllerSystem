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
import kotlinx.android.synthetic.main.curretn_wallet_item.view.*


class CurrentWalletAdapter(
    var context: Context,
    var currentWalletList: ArrayList<Data>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener
) :
    RecyclerView.Adapter<CurrentWalletAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.curretn_wallet_item, parent, false)
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


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(
            orderWallet: Data,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.currentWalletName.text = " أمر تجهيز رقم ${orderWallet?.order_number}"
            itemView.price.text = orderWallet?.total_price ?: ""
            itemView.currancy.text = orderWallet?.currency ?: ""
            if (orderWallet?.details?.isNullOrEmpty() == false)
            {
                itemView.walletCategoryTxt?.text =
                    orderWallet?.details?.get(0)?.product?.category?.name ?: ""

                itemView.currentWalletDesc.text = orderWallet?.details?.get(0)?.product?.name ?: ""

                itemView.quantity.text = (orderWallet?.details?.get(0)?.quantity ?: 0).toString() ?: ""

            }
            // itemView.walletStoreTxt?.text = orderWallet?.currency?:""
            itemView.slash?.visibility = View.GONE
            itemView.walletStoreTxt?.visibility = View.GONE
            // itemView.walletCategoryTxt?.visibility = View.GONE
            itemView.statusCurrentWalletTxt?.text = orderWallet?.status_word ?: ""


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