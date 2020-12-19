package com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.admin.storesproducts.models.ProductsModel
import kotlinx.android.synthetic.main.delegate_order_item.view.*

class DelegateOrdersAdapter(
    var context: Context,
    var productList: ArrayList<ProductsModel> ,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<DelegateOrdersAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.delegate_order_item , parent , false)
        return ViewHolder(view)
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
            productsModel: ProductsModel,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.orderNumber.text = productsModel.desc
            itemView.orderName.text = productsModel.name
            itemView.orderPrice.text = productsModel.price
            itemView.orderQuantity.text = productsModel.quantity.toString()


            if (productsModel.isSelected == 0)
            {
                itemView.statusDesc.text = itemView.context.getString(R.string.waiting)
                itemView.statusDesc.setTextColor(ContextCompat.getColor(itemView.context , R.color.orange_light))
                itemView.statusImg.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.color.orange_light))
            }
            else{
                itemView.statusDesc.text = itemView.context.getString(R.string.delivered)
                itemView.statusDesc.setTextColor(ContextCompat.getColor(itemView.context , R.color.text_green))
                itemView.statusImg.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.color.text_green))

            }

            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}