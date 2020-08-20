package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.stored_item_adaptor.view.*

class StoredClassificationAdaptor(val modelData: ViewModelHandleChangeFragmentclass,
                                  val arrayListOfTutorials:ArrayList<Any>//this method is returning the view for each item in the list
) : RecyclerView.Adapter<StoredClassificationAdaptor.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stored_item_adaptor, parent, false)
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
            itemData: Any,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<Any>
        ) {

            /*   itemView.costText.text = itemData.modelCost
                if (itemData.modelStatus==myOrdersModel.doneOrder) {
                    itemView.statusOfOrder.setTextColor(ContextCompat.getColor(itemView.context,R.color.green))
                } else if(itemData.modelStatus==myOrdersModel.canceled) {

                    itemView.statusOfOrder.setTextColor(ContextCompat.getColor(itemView.context,R.color.red))

                }*/
            //   Glide.with(itemView.imageItem.context).load(itemData.image).into(itemView.imageItem)
            ///  itemView.nameTextView.text = itemData.name?:""
            // itemView.simpleRatingBar.rating = 4f
            // itemView.tutorialImage = itemData.t?:""

            /*  if (adapterPosition ==arrayListOffersValues.size-1) {
                  itemView.divider.visibility = View.GONE
              }*/

            if (adapterPositionChecked ==adapterPosition) {
                showHideViews(itemView,View.VISIBLE)

            }
            else {
                showHideViews(itemView,View.GONE)
            }

            itemView?.decreaseImage.setOnClickListener{
                if (Integer.parseInt(itemView?.increasementText?.text.toString())>1)
                    itemView?.increasementText?.text = (Integer.parseInt(itemView?.increasementText?.text.toString())-1).toString()
            }
            itemView?.plusImage.setOnClickListener{
                    itemView?.increasementText?.text = (Integer.parseInt(itemView?.increasementText?.text.toString())+1).toString()
            }
            itemView.itemContainer.setOnClickListener{

                  onItemClicked(modelData,adapterPosition) // go to details please
              }


        }

        private fun showHideViews(itemView: View, showGone: Int) {
            itemView?.checked?.visibility = showGone
            itemView?.containerNumber.visibility = showGone
        }


        private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
             adapterPositionChecked = adapterPosition
              // send this item please
              model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please
             notifyDataSetChanged()


          }

    }
    var adapterPositionChecked = -1 // this is the checked item to use

}