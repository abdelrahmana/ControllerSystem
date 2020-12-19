package com.smartangle.controllersystemapp.accountant.makeorder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.categories.models.Data
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.product_item_adaptor.view.*

class AccountantLastCategoriesAdaptor(val modelData: ViewModelHandleChangeFragmentclass,
                                      val arrayListOfTutorials:ArrayList<Data>//this method is returning the view for each item in the list
) : RecyclerView.Adapter<AccountantLastCategoriesAdaptor.ViewHolder>()  {

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

            itemView.itemContainer.setOnClickListener{
              //  itemView.checked.visibility = View.VISIBLE
                  onItemClicked(modelData,adapterPosition) // go to details please
              }


        }


         private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
              // send this item please
              model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please


          }

    }

    companion object{

        var selctedPosition = -1

    }

}