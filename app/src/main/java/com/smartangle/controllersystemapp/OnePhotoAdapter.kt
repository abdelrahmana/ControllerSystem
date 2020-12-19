package com.smartangle.controllersystemapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.one_photoscolleg_row.view.*
import java.util.*

class OnePhotoAdapter(var models: ArrayList<String>) : RecyclerView.Adapter<OnePhotoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.one_photoscolleg_row, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: String = models[position]
        holder.updateImage(model)
    }

    override fun getItemCount(): Int {
        return models.size
    }
//, View.OnClickListener
    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        fun updateImage(model: String) {

            Glide.with(itemView.context).load(model)
                   // .error(R.drawable.no_profile).placeholder(R.drawable.no_profile)
                .into(itemView.photo_imageView_all_photos_zoom)





//            itemView.pic_loader?.visibility = View.VISIBLE
//            Glide.with(itemView.context)
//                    .asBitmap()
//                    .error(R.drawable.no_profile)
//                    .placeholder(R.drawable.no_profile)
//                    .load(model).diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(object : CustomTarget<Bitmap?>() {
//                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap?>?) {
//                    itemView.photo_imageView_all_photos_zoom?.setImageBitmap(bitmap)
//                    itemView.pic_loader?.visibility = View.GONE
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    itemView.pic_loader?.visibility = View.GONE
//                }
//            })
        }

//        override fun onClick(view: View) {
//
//
//
//        }


    }

}
