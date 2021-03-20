package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.reports.model.Data
import com.smartangle.controllersystemapp.admin.reports.model.Datax
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.sales_item_adapter.view.*

class PurchaseAdapterFragment(val modelData: ViewModelHandleChangeFragmentclass,
                              val arrayListOfTutorials:ArrayList<Datax>//this method is returning the view for each item in the list
) : RecyclerView.Adapter<PurchaseAdapterFragment.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sales_item_adapter, parent, false)
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
            itemData: Datax,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<Datax>
        ) {
            itemView.deliveryNumberText?.append(" "+itemData.purchases_value?:"")
            itemView.rsomText?.text = itemData?.price?:""
            itemView.nameTextView?.text = itemData.name?:""
            //itemView.date?.text = UtilKotlin.getFormatedDate(itemData.created_at?:"",itemView.context)
            itemView.piecesTextView?.text =  itemData.total_quantity?:""
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

            itemView.cardContainer.setOnClickListener{

                  onItemClicked(modelData,adapterPosition) // go to details please
              }


        }


         private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
              // send this item please
              model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please


          }

    }

}