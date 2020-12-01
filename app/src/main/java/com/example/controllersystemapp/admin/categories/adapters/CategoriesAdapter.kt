package com.example.controllersystemapp.admin.categories.adapters

import android.content.Context
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

class CategoriesAdapter(val modelData: ViewModelHandleChangeFragmentclass,
                        var cateogriesList: ArrayList<Data>,
                        var onRecyclerItemClickListener: OnRecyclerItemClickListener
                        ) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_category_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cateogriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(cateogriesList[position])


    }


    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            categoryModel: Data) {

            itemView.category_name?.text = categoryModel.name?:""
           // itemView.categoryCount.text = categoryModel.products_count?:""


            itemView.setOnClickListener {
                Log.d("Click" , "observeData")

                //onRecyclerItemClickListener.onItemClick(adapterPosition)
                modelData.setNotifyItemSelected(cateogriesList[adapterPosition]) // update sign up fragment please

            }

            itemView.editImage?.setOnClickListener {
                onRecyclerItemClickListener?.onItemClick(adapterPosition)
            }





        }


    }


}