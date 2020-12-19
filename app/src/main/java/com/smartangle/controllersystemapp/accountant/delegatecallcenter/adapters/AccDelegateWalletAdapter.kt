package com.smartangle.controllersystemapp.accountant.delegatecallcenter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.debts.AccountantDebtsList
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.aac_delegate_wallet_item.view.*

class AccDelegateWalletAdapter(
    var context: Context,
    var accDelegateWalletList: ArrayList<AccountantDebtsList>,
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
            accountantDebtsList: AccountantDebtsList,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {


            itemView.walletProdName.text = accountantDebtsList.product?.name?:""
            itemView.statusT.text = accountantDebtsList.status_word?:""
            itemView.walletProdPrice.text = accountantDebtsList.product?.price?:""
            itemView.walletProdQuantity.text = accountantDebtsList.quantity?:""
            itemView.walletProdCurrancy.text = accountantDebtsList.product?.currency?:""
            itemView.editDebtsBtn.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}