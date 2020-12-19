package com.smartangle.controllersystemapp.accountant.settings.expenses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemWithLongClickListener
import kotlinx.android.synthetic.main.accountant_expenses_item.view.*

class AccountantExpensesAdapter(
    var context: Context,
    var expensesList: ArrayList<Data>,
    var onRecyclerItemClickListener: OnRecyclerItemWithLongClickListener
) :
    RecyclerView.Adapter<AccountantExpensesAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.accountant_expenses_item , parent , false)
        return ViewHolder(view)
    }

    fun removeItemFromList(position: Int) {
        expensesList?.let {
            expensesList?.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(expensesList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            productsModel: Data,
            onRecyclerItemClickListener: OnRecyclerItemWithLongClickListener
        ) {

//            Glide.with(itemView.context).load(productsModel.image?:"")
////                .placeholder(R.drawable.no_profile)
////                .error(R.drawable.no_profile)
//                .into(itemView.productImg)
            itemView.expensesName.text = productsModel.title?:""
            itemView.expensesDetails.text = productsModel.details?:""
            itemView.expensesPrice.text = productsModel.price?:""



            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }

            itemView.setOnLongClickListener {

                onRecyclerItemClickListener.onItemLongClick(adapterPosition)
                return@setOnLongClickListener true
            }



        }


    }


}