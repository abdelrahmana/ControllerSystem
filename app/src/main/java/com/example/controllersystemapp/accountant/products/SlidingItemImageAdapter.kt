package com.example.controllersystemapp.accountant.products

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import kotlinx.android.synthetic.main.viewpager_slide_item.view.*


class SlidingItemImageAdapter(context: Context, IMAGES: ArrayList<Any>) :
    PagerAdapter() {
    private val IMAGES: ArrayList<Any>
    private val context: Context
    lateinit var layoutInflater: LayoutInflater

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {

        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


        val imageLayout: View = layoutInflater.inflate(R.layout.viewpager_slide_item, view, false)!!
        val imageView: ImageView = imageLayout
            .findViewById(R.id.image) as ImageView
        //view.image?.setImageResource(IMAGES[position])


        //Glide.with(context!!).load(IMAGES[position].image).into(imageView)

        view.addView(imageLayout, 0)

        imageLayout.image?.setOnClickListener {
           // Toast.makeText(context , "clicked" , Toast.LENGTH_LONG).show()
            Log.d("click" , "slider")

        }
        return imageLayout
    }

//     fun isViewFromObject(view: View, obj: Any?): Boolean {
//        return view.equals(obj)
//    }

    override fun restoreState(
        state: Parcelable?,
        loader: ClassLoader?
    ) {
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view.equals(obj)
    }

    init {
        this.context = context
        this.IMAGES = IMAGES
      //  layoutInflater = LayoutInflater.from(context)
    }
}