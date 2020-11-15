package com.example.controllersystemapp.admin.storesproducts.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.ViewImagesGalleryActivity
import com.example.controllersystemapp.admin.storesproducts.models.Image
import com.makeramen.roundedimageview.RoundedImageView
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.ArrayList


class ProductListSliderAdapter(var context: Context, val imageList: List<Image>):
    SliderViewAdapter<ProductListSliderAdapter.SliderAdapterVH>() {


    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {

        val inflate: View = LayoutInflater.from(parent!!.context).inflate(R.layout.image_slider_row, null)
        return SliderAdapterVH(inflate)
    }

    override fun getCount(): Int {
       return imageList.size
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {

        Glide.with(context).load(imageList[position].image?:"")
//            .centerCrop().error(R.drawable.no_profile)
//            .placeholder(R.drawable.no_profile)
            .dontAnimate().into(viewHolder!!.imaViewSlider)


        viewHolder.itemView?.setOnClickListener {

            val images : ArrayList<String> = ArrayList()
            for (i in 0 until imageList?.size!!) {

                images.add(imageList[i]?.image?:"")

            }

            val intent = Intent(context, ViewImagesGalleryActivity::class.java)
            intent.putStringArrayListExtra("imagesList", images as ArrayList<String>?)
            intent.putExtra("position", position)
            context?.startActivity(intent)
        }

    }


    class SliderAdapterVH(itemView : View) : SliderViewAdapter.ViewHolder(itemView) {

        var itemView: View? = null
        var imaViewSlider : RoundedImageView = itemView.findViewById(R.id.slider_image_row)

        init {
            this.itemView = itemView
        }

    }



}