package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnStoreSelcteClickListener
import com.example.controllersystemapp.admin.storesproducts.models.StoresData
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.stored_item_adaptor.view.*

class StoredClassificationAdaptor(val modelData: ViewModelHandleChangeFragmentclass,
                                  val arrayListOfTutorials:ArrayList<StoresData>
    //this method is returning the view for each item in the list
, val listener: OnStoreSelcteClickListener
) : RecyclerView.Adapter<StoredClassificationAdaptor.ViewHolder>()  {

    var quantityList = ArrayList<Int>()
    var storesIdList = ArrayList<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stored_item_adaptor, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayListOfTutorials[position],/*modelData*/arrayListOfTutorials,listener)
        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrayListOfTutorials.size
    }



    //the class is hodling the list view
    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(
            itemData: StoresData,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<StoresData>,
            listener: OnStoreSelcteClickListener
        ) {

            itemView.store_name.text = itemData?.name?:""
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

//            if (adapterPositionChecked == adapterPosition) {
//                showHideViews(itemView,View.VISIBLE)
//
//            }
//            else {
//                showHideViews(itemView,View.GONE)
//            }

            itemView?.decreaseImage.setOnClickListener{
                if (Integer.parseInt(itemView?.increasementText?.text.toString())>1)
                    itemView?.increasementText?.text = (Integer.parseInt(itemView?.increasementText?.text.toString())-1).toString()

               // listener?.onDecreaseItemClick(adapterPosition, itemView?.increasementText?.text.toString()?:"")

            }
            itemView?.plusImage.setOnClickListener{
                    itemView?.increasementText?.text = (Integer.parseInt(itemView?.increasementText?.text.toString())+1).toString()
               // listener?.onIncreaseItemClick(adapterPosition, itemView?.increasementText?.text.toString()?:"")


            }
            itemView.itemContainer.setOnClickListener{

                  onItemClicked(modelData,adapterPosition) // go to details please
              }


        }

        private fun showHideViews(itemView: View, showGone: Int) {
            Log.d("pos" , "showw")

            itemView?.checked?.visibility = showGone
            itemView?.containerNumber.visibility = showGone
        }


        private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
            Log.d("pos" , "$position")
            // adapterPositionChecked = adapterPosition
            showHideViews(itemView,View.VISIBLE)
//            quantityList.clear()
//            storesIdList.clear()
            quantityList.add((itemView.increasementText?.text.toString()).toInt())
            storesIdList.add(arrayListOfTutorials.get(position).id?:-1)

            listener.onClickItemClick(position, quantityList , storesIdList )
//            for (i in quantityList.indices) {
//
//                arrayListOfTutorials[position].quantityList?.add(quantityList[i])
//
//            }
//
//            for (i in storesIdList.indices) {
//
//                arrayListOfTutorials[position].storesIdList?.add(storesIdList[i])
//
//            }

            Log.d("Arraysss" , "${quantityList.size}")
            Log.d("Arraysss" , "${storesIdList.size}")

            // send this item please
            // model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please
             //notifyDataSetChanged()


          }

    }
    var adapterPositionChecked = -1 // this is the checked item to use

}