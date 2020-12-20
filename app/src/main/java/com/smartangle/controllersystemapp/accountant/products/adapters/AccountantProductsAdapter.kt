package com.smartangle.controllersystemapp.accountant.products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.products.models.Data
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.products_item.view.*

class AccountantProductsAdapter(
    var context: Context,
    var productList: ArrayList<Data>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<AccountantProductsAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.products_item , parent , false)
        return ViewHolder(
            view
        )
    }

    fun removeItemFromList(position: Int) {
        productList?.let {
            productList?.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(productList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            productsModel: Data,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            Glide.with(itemView.context).load(productsModel.image?:"")
//                .placeholder(R.drawable.no_profile)
//                .error(R.drawable.no_profile)
                .into(itemView.productImg)
            itemView.productName.text = productsModel.name?:""
            itemView.productDesc.text = productsModel.category?.name?:""
            itemView.price.text = productsModel.price?:""
            itemView.quantity.text = productsModel.total_quantity?:""
            itemView.currancy.text = productsModel.currency?:""


            //get from api
//            if (productsModel.isSelected == 0)
//            {
//                itemView.statusText.text = itemView.context.getString(R.string.un_selected)
//                itemView.status.setTextColor(ContextCompat.getColor(itemView.context , R.color.login_gray))
//                itemView.statusText.setTextColor(ContextCompat.getColor(itemView.context , R.color.login_gray))
//                itemView.circleImg.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.color.login_gray))
//            }
//            else{
//                itemView.statusText.text = itemView.context.getString(R.string.selected)
//                itemView.status.setTextColor(ContextCompat.getColor(itemView.context , R.color.green))
//                itemView.statusText.setTextColor(ContextCompat.getColor(itemView.context , R.color.green))
//                itemView.circleImg.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.color.green))
//
//            }

            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}