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
import com.example.controllersystemapp.admin.storesproducts.models.StoresModel
import kotlinx.android.synthetic.main.products_item.view.*
import kotlinx.android.synthetic.main.stores_item.view.*

class StoresAdapter(
    var context: Context,
    var storeList: ArrayList<StoresModel> ,
    var onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<StoresAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.stores_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(storeList[position], onItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            storesModel: StoresModel,
            onItemClickListener: OnItemClickListener
        ) {

            itemView.storeName.text = storesModel.name
            itemView.storeAddress.text = storesModel.address
            itemView.storeQuantity.text = storesModel.quantity.toString()
            itemView.belongName.text = storesModel.personName.toString()

            itemView.setOnClickListener {

                onItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}