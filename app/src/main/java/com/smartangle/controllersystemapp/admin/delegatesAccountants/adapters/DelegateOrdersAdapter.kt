package com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.DelegateOrder
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.admin.storesproducts.models.ProductsModel
import kotlinx.android.synthetic.main.acc_delegate_order_item.view.*

class DelegateOrdersAdapter(
    var context: Context,
    var productList: ArrayList<DelegateOrder>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<DelegateOrdersAdapter.ViewHolder>(){


//delegate_order_item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.acc_delegate_order_item , parent , false)
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
            delegateOrder: DelegateOrder,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

//            itemView.orderNumber.text = productsModel.desc
//            itemView.orderName.text = productsModel.name
//            itemView.orderPrice.text = productsModel.price
//            itemView.orderQuantity.text = productsModel.quantity.toString()

            itemView.orderNumber.text = "أمر تجهيز رقم${delegateOrder?.order_number?:""}"
            // itemView.orderName.text = delegateOrder.name
            itemView.orderPrice.text = delegateOrder.total_price?:""
            itemView.orderCurrancy.text = delegateOrder.currency?:""
            // itemView.orderQuantity.text = delegateOrder.quantity.toString()


            if (delegateOrder?.details?.isNullOrEmpty() == false)
            {
                itemView.orderName.text = delegateOrder?.details?.get(0)?.product?.name?:""
            }


//            if (productsModel.isSelected == 0)
//            {
//                itemView.statusDesc.text = itemView.context.getString(R.string.waiting)
//                itemView.statusDesc.setTextColor(ContextCompat.getColor(itemView.context , R.color.orange_light))
//                itemView.statusImg.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.color.orange_light))
//            }
//            else{
//                itemView.statusDesc.text = itemView.context.getString(R.string.delivered)
//                itemView.statusDesc.setTextColor(ContextCompat.getColor(itemView.context , R.color.text_green))
//                itemView.statusImg.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.color.text_green))
//
//            }

            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}