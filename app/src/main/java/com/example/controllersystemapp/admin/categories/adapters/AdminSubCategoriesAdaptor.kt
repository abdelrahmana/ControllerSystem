package com.example.controllersystemapp.admin.categories.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.admin_category_item.view.*

class AdminSubCategoriesAdaptor(val modelData: ViewModelHandleChangeFragmentclass,
                                val arrayListOfTutorials:ArrayList<Data>//this method is returning the view for each item in the list
                                , var onRecyclerItemClickListener: OnRecyclerItemClickListener
) : RecyclerView.Adapter<AdminSubCategoriesAdaptor.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.admin_category_item, parent, false)
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

            itemView.category_name?.text = itemData.name?:""


            itemView.itemContainer.setOnClickListener{

                  onItemClicked(modelData,adapterPosition) // go to details please
              }

            itemView.editImage?.setOnClickListener {
                onRecyclerItemClickListener?.onItemClick(adapterPosition)

            }

        }


         private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
              // send this item please
              model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please


          }

    }

}