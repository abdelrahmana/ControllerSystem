package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.categories.models.Data
import com.smartangle.controllersystemapp.admin.interfaces.OnCategoriesSelectedelcteClickListener
import com.smartangle.util.ViewModelHandleChangeFragmentclass
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

    val selected: ArrayList<Data> = ArrayList()


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
             arrayListOfTutorials[position].isChecked = !arrayListOfTutorials[position].isChecked
             if (arrayListOfTutorials[position].isChecked)
             {
                 showHideViews(itemView,View.VISIBLE)
                 selected.add(arrayListOfTutorials[position])
             }
             else{
                 showHideViews(itemView,View.GONE)
                 selected.remove(arrayListOfTutorials[position])
             }


//             showHideViews(itemView,View.VISIBLE)
//             categoriesList.add(arrayListOfTutorials[position].id?:-1)
//             categoriesListNames.add(arrayListOfTutorials[position].name?:"")
//
             listner.onItemClickData(position , selected)

          }

    }

}