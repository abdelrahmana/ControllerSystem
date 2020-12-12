package com.example.controllersystemapp.accountant.delegatecallcenter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.aac_delegate_wallet_item.view.*

class AccDelegateWalletAdapter(
    var context: Context,
    var accDelegateWalletList: ArrayList<Any> ,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<AccDelegateWalletAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.aac_delegate_wallet_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return accDelegateWalletList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(accDelegateWalletList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            productsModel: Any,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

//            itemView.walletProdName.text = productsModel.name
//            itemView.walletProdDesc.text = productsModel.desc
//            itemView.walletProdPrice.text = productsModel.price
//            itemView.walletProdQuantity.text = productsModel.quantity.toString()
//            itemView.walletProdCurrancy.text = productsModel.currancy
            itemView.editDebtsBtn.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}