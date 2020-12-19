package com.smartangle.controllersystemapp.common.cities

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.adaptor_item_country_city.view.*

class CityAdapter(val modelData: ViewModelHandleChangeFragmentclass,
                  val arrayListOffersValues:ArrayList<Cities>//this method is returning the view for each item in the list
) : RecyclerView.Adapter<CityAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adaptor_item_country_city, parent, false)
        return ViewHolder(v)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayListOffersValues[position],modelData,arrayListOffersValues)
        //  setAnimation(holder.itemView, position)


    }




    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrayListOffersValues.size
    }



    //the class is hodling the list view
    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(
            itemData: Cities,
            model: ViewModelHandleChangeFragmentclass,
            arrayListOffersValues: ArrayList<Cities>
        ) {
            itemView.itemID.text = itemData.name?:""

            if (adapterPosition ==arrayListOffersValues.size-1) {
                // last item
                itemView.divider.visibility = View.GONE
            }

            itemView.countryHolder.setOnClickListener{

                onItemClicked(model,adapterPosition) // go to details please
            }


        }


        private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
            // send this item please
            Log.d("TAG" , "click")

            model.responseCodeDataSetter(arrayListOffersValues.get(position)) // update sign up fragment please


        }

    }

}