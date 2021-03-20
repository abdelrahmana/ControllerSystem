package com.smartangle.controllersystemapp.callcenter.maketalbya.productclassification.lastsubcategory.productclassification.lastsubcategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.callcenter.maketalbya.productclassification.lastsubcategory.productmodel.Datas
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.product_item_adaptor.view.*
import kotlinx.android.synthetic.main.product_item_adaptor.view.checked
import kotlinx.android.synthetic.main.product_item_adaptor.view.containerNumber
import kotlinx.android.synthetic.main.product_item_adaptor.view.decreaseImage
import kotlinx.android.synthetic.main.product_item_adaptor.view.increasementText
import kotlinx.android.synthetic.main.product_item_adaptor.view.itemContainer
import kotlinx.android.synthetic.main.product_item_adaptor.view.plusImage

class LastSubProductAdaptorCenter(val modelData: ViewModelHandleChangeFragmentclass,
                                          val arrayListOfTutorials:ArrayList<Datas>//this method is returning the view for each item in the list
) : RecyclerView.Adapter<LastSubProductAdaptorCenter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.product_item_adaptor, parent, false)
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
            itemData: Datas,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<Datas>
        ) {

            itemView.category_name.text = itemData.name?:""

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
            itemView?.decreaseImage.setOnClickListener{
                if (Integer.parseInt(itemView?.increasementText?.text.toString())>1)
                    itemView?.increasementText?.setText((Integer.parseInt(itemView?.increasementText?.text.toString())-1).toString())
                // listener?.onDecreaseItemClick(adapterPosition, itemView?.increasementText?.text.toString()?:"")
            }
            itemView?.plusImage.setOnClickListener{
                itemView?.increasementText?.setText( (Integer.parseInt(itemView?.increasementText?.text.toString())+1).toString())
                // listener?.onIncreaseItemClick(adapterPosition, itemView?.increasementText?.text.toString()?:"")


            }
            if (adapterPositions== adapterPosition) {
                itemView.checked?.visibility = View.VISIBLE
                itemView.containerNumber?.visibility = View.VISIBLE

            }
            else {
                itemView.checked?.visibility = View.GONE
                itemView.containerNumber?.visibility = View.GONE
            }

            itemView.itemContainer.setOnClickListener{
                 adapterPositions = adapterPosition
                  onItemClicked(modelData,adapterPosition,(itemView.increasementText?.text.toString())?:"1") // go to details please
                notifyDataSetChanged()

            }


        }


         private fun onItemClicked(
             model: ViewModelHandleChangeFragmentclass,
             position: Int,
             quantity: String
         ) {
              // send this item please
             arrayListOfTutorials.get(position).totalSelectedProduct = quantity
              model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please


          }

    }
    var adapterPositions = -1

}