package com.example.controllersystemapp.admin.storesproducts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.storesproducts.models.Image
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.image_itemadaptor.view.*

class ImagesEditListAdaptor(
    val modelData: ViewModelHandleChangeFragmentclass, var arrayListOfImagessValues: ArrayList<Image> //this method is returning the view for each item in the list
) : RecyclerView.Adapter<ImagesEditListAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_itemadaptor, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    //    holder.bindItems(imageModel.image!![position],/*modelData*/imageModel.image!!)
        holder.bindItems(arrayListOfImagessValues[position])

        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrayListOfImagessValues.size //imageModel.image!!.size
    }


    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(
                itemData: Image
        ) {

            Glide.with(itemView.context).load(itemData.image?:"").into(itemView.imageItem)

            // even this not used by the way
        /*    if (defaultSelectedItem == itemData.id) {
                itemView.clinicCardMaterial.setStrokeColor(ContextCompat.getColorStateList(itemView.context, R.color.skyColor))
            } else {
                itemView.clinicCardMaterial.setStrokeColor(ContextCompat.getColorStateList(itemView.context, R.color.white))
            }*/
            itemView.removeImage.setOnClickListener {
                itemView.removeCard.performClick()
            }
            itemView.removeCard.setOnClickListener {
                // itemView.clinicMaterialCard.setStrokeColor( ContextCompat.getColorStateList(itemView.context!!, R.color.skyColor))
                // not needed actually
                // arrayListOffersValues.get(defaultSelectedItem)
                // this@DoctorCategoryAdaptor.
                // defaultSelectedItem = adapterPosition
                onItemClicked(modelData, adapterPosition) // change inner recycle depend on selected clinic
                //  notifyDataSetChanged()
            }


        }


        private fun onItemClicked(model: ViewModelHandleChangeFragmentclass, position: Int) {
            // send this item please
            // set clicked to specific item
           // imageModel.image?.removeAt(position)

          //  arrayListOfImagessValues.removeAt(position)
            arrayListOfImagessValues.get(position).position = position // set it to position before delete it
            // notifyDataSetChanged() // update list
           // model.setNotifiedItemSelected(arrayListOfImages.get(position)) // update sign up fragment please
            // go to clinic fragment with this id please
            model.setNotifyItemSelected(arrayListOfImagessValues.get(position)) // notify others to this please


        }

    }

//    var defaultSelectedItem = -1 // defqult no thing selected


}