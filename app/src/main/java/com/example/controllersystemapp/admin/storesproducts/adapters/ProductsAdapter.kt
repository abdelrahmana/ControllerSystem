package com.example.controllersystemapp.admin.storesproducts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnItemClickListener
import com.example.controllersystemapp.admin.storesproducts.models.ProductsModel
import kotlinx.android.synthetic.main.products_item.view.*

class ProductsAdapter(
    var context: Context,
    var productList: ArrayList<ProductsModel> ,
    var onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.products_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(productList[position], onItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            productsModel: ProductsModel,
            onItemClickListener: OnItemClickListener
        ) {

            itemView.productName.text = productsModel.name
            itemView.productDesc.text = productsModel.desc
            itemView.price.text = productsModel.price
            itemView.quantity.text = productsModel.quantity.toString()


            if (productsModel.isSelected == 0)
            {
                itemView.statusText.text = itemView.context.getString(R.string.un_selected)
                itemView.status.setTextColor(ContextCompat.getColor(itemView.context , R.color.login_gray))
                itemView.statusText.setTextColor(ContextCompat.getColor(itemView.context , R.color.login_gray))
                itemView.circleImg.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.color.login_gray))
            }
            else{
                itemView.statusText.text = itemView.context.getString(R.string.selected)
                itemView.status.setTextColor(ContextCompat.getColor(itemView.context , R.color.green))
                itemView.statusText.setTextColor(ContextCompat.getColor(itemView.context , R.color.green))
                itemView.circleImg.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.color.green))

            }

            itemView.setOnClickListener {

                onItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}