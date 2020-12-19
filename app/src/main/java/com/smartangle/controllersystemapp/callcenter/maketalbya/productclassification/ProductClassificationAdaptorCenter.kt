package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.categories.models.Data
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.product_item_adaptor.view.*

class ProductClassificationAdaptorCenter(val modelData: ViewModelHandleChangeFragmentclass,
                                   val arrayListOfTutorials:ArrayList<Data>//this method is returning the view for each item in the list
) : RecyclerView.Adapter<ProductClassificationAdaptorCenter.ViewHolder>()  {

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
            itemData: Data,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<Data>
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

            itemView.itemContainer.setOnClickListener{

                  onItemClicked(modelData,adapterPosition) // go to details please
              }


        }


         private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
              // send this item please
             Log.d("paretnId" , "clickAdapter")

//             val bundle = Bundle()
//             //bundle.putInt(EXITENCEIDPACKAGE, availableServiceList.get(position).id?:-1)
//             bundle.putInt(FragmentProductclassification.PARENT_ID, arrayListOfTutorials[position].id?:-1)
//             bundle.putString(FragmentProductclassification.PARENT_NAME, arrayListOfTutorials[position].name?:"")
//
//             UtilKotlin.changeFragmentWithBack(
//                 (itemView.context as FragmentActivity?)!!,
//                 R.id.frameLayout_direction,
//                 FragmentSubProductclassification(),
//                 bundle
//             )
             model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please


          }

    }

}