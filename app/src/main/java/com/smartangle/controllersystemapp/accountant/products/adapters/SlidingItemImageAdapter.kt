package com.smartangle.controllersystemapp.accountant.products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.products.models.Image
import kotlinx.android.synthetic.main.viewpager_slide_item.view.*


class SlidingItemImageAdapter(var context: Context, var IMAGES: ArrayList<Image>) :
    RecyclerView.Adapter<SlidingItemImageAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.viewpager_slide_item, parent, false)
        return MyViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(context!!).load(IMAGES[position].image).into(holder.itemView.image)
    }

    override fun getItemCount(): Int {
        return IMAGES.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
//        var tvName: TextView
//
//        init {
//            tvName = itemView.findViewById(R.id.tvName)
//        }
    }



}