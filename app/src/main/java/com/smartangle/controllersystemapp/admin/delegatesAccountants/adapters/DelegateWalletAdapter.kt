package com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.admin.storesproducts.models.ProductsModel
import kotlinx.android.synthetic.main.delegate_wallet_item.view.*

class DelegateWalletAdapter(
    var context: Context,
    var delegateWalletList: ArrayList<ProductsModel> ,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<DelegateWalletAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.delegate_wallet_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return delegateWalletList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(delegateWalletList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            productsModel: ProductsModel,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.walletProdName.text = productsModel.name
            itemView.walletProdDesc.text = productsModel.desc
            itemView.walletProdPrice.text = productsModel.price
            itemView.walletProdQuantity.text = productsModel.quantity.toString()
            itemView.walletProdCurrancy.text = productsModel.currancy
            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}