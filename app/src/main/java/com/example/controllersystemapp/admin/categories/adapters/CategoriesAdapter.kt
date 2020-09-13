package com.example.controllersystemapp.admin.categories.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.categories_item.view.*

class CategoriesAdapter(
    var cateogriesList: ArrayList<Data>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cateogriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(cateogriesList[position], onRecyclerItemClickListener)


    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            categoryModel: Data,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {

            itemView.categoryName.text = categoryModel.name?:""
            itemView.categoryCount.text = categoryModel.products_count?:""


            itemView.setOnClickListener {

                onRecyclerItemClickListener.onItemClick(adapterPosition)

            }





        }


    }


}