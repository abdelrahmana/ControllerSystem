package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.interfaces.OnCategoriesSelectedelcteClickListener
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.product_item_adaptor.view.*
import kotlinx.android.synthetic.main.product_item_adaptor.view.checked
import kotlinx.android.synthetic.main.product_item_adaptor.view.itemContainer

class ProductCategoriesSelectAdaptor(val modelData: ViewModelHandleChangeFragmentclass,
                                     val arrayListOfTutorials:ArrayList<Data>
    //this method is returning the view for each item in the list
, val listner : OnCategoriesSelectedelcteClickListener
) : RecyclerView.Adapter<ProductCategoriesSelectAdaptor.ViewHolder>()  {

    var categoriesList = ArrayList<Int>()
    var categoriesListNames = ArrayList<String>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.product_item_adaptor, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayListOfTutorials[position],/*modelData*/arrayListOfTutorials ,listner)
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
            arrayListOffersValues: ArrayList<Data>,
            listner : OnCategoriesSelectedelcteClickListener
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

                  onItemClicked(modelData,adapterPosition , listner) // go to details please
              }


        }

        private fun showHideViews(itemView: View, showGone: Int) {
            Log.d("pos" , "showw")

            itemView?.checked?.visibility = showGone
           // itemView?.containerNumber.visibility = showGone
        }

         private fun onItemClicked(
             model: ViewModelHandleChangeFragmentclass,
             position: Int,
             listner: OnCategoriesSelectedelcteClickListener
         ) {
              // send this item please
              //model.setNotifyItemSelected(arrayListOfTutorials.get(position)) // update sign up fragment please
             showHideViews(itemView,View.VISIBLE)
             categoriesList.add(arrayListOfTutorials[position].id?:-1)
             categoriesListNames.add(arrayListOfTutorials[position].name?:"")

             listner.onClickItemClick(position , categoriesList , categoriesListNames)

          }

    }

}