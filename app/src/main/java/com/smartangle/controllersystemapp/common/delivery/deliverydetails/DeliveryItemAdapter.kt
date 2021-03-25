package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.common.delivery.Data
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.admin_order_list.view.*

class DeliveryItemAdapter(val modelData: ViewModelHandleChangeFragmentclass,
                          val arrayListOfTutorials:ArrayList<Data>//this method is returning the view for each item in the list
) : RecyclerView.Adapter<DeliveryItemAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //R.layout.delivery_item_adapter
        val v = LayoutInflater.from(parent.context).inflate(R.layout.admin_order_list, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayListOfTutorials[position],/*modelData*/arrayListOfTutorials)
        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrayListOfTutorials.size
    }



    //the class is hodling the list view
    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(
            itemData: Data,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<Data>
        ) {

            itemView.newWalletName.text = " أمر تجهيز رقم ${itemData?.order_number}"
            itemView.newOrderPrice.text = itemData?.total_price?:""
            itemView.newOrderCurrency.text = itemData?.currency?:""
            // itemView.walletStoreTxt?.text = orderWallet?.currency?:""
            if (itemData?.details?.isNullOrEmpty() == false)
            {
                itemView.newWalletDesc.text = itemData?.details?.get(0)?.product?.name?:""
                itemView.newOrderQuantity.text = (itemData?.details?.get(0)?.quantity?:0).toString()?:""
                //itemView.newwalletCategoryTxt?.text = itemData?.details?.get(0)?.product?.category?.name?:""
            }
//            itemView.newSlash?.visibility = View.GONE
//            itemView.newwalletStoreTxt?.visibility = View.GONE

            /*   itemView.costText.text = itemData.modelCost
                if (itemData.modelStatus==myOrdersModel.doneOrder) {
                    itemView.statusOfOrder.setTextColor(ContextCompat.getColor(itemView.context,R.color.green))
                } else if(itemData.modelStatus==myOrdersModel.canceled) {

                    itemView.statusOfOrder.setTextColor(ContextCompat.getColor(itemView.context,R.color.red))

                }*/
            //   Glide.with(itemView.imageItem.context).load(itemData.image).into(itemView.imageItem)
            ///  itemView.nameTextView.text = itemData.name?:""
            // itemView.simpleRatingBar.rating = 4f
            // itemView.tutorialImage = itemData.t?:""

            /*  if (adapterPosition ==arrayListOffersValues.size-1) {
                  itemView.divider.visibility = View.GONE
              }*/

            itemView.setOnClickListener{

                  onItemClicked(modelData,adapterPosition) // go to details please
              }


        }


         private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
              // send this item please
              model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please


          }

    }

}