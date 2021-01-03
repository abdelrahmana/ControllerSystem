package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnStoreSelcteClickListener
import com.smartangle.controllersystemapp.admin.storesproducts.models.StoresData
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.stored_item_adaptor.view.*


class StoredClassificationAdaptor(val modelData: ViewModelHandleChangeFragmentclass,
                                  val arrayListOfTutorials:ArrayList<StoresData>
    //this method is returning the view for each item in the list
, val listener: OnStoreSelcteClickListener
) : RecyclerView.Adapter<StoredClassificationAdaptor.ViewHolder>()  {

    var quantityList = ArrayList<Int>()
    var storesIdList = ArrayList<Int>()
    var clickFlag = false
    var clickRowFlag = false


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

    fun getSelected(): ArrayList<StoresData>? {
        val selected: ArrayList<StoresData> = ArrayList()
        for (i in 0 until arrayListOfTutorials.size) {
            if (arrayListOfTutorials.get(i).isChecked!!) {
                selected.add(arrayListOfTutorials[i])
            }
        }
        return selected
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

            itemView?.productNameEditText?.setText((itemData.quantity?:1).toString())
            itemView?.productNameEditText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {

                    if (s.length != 0) {

                       // modelData.setNotifyItemSelected()
                        itemData.quantity = (itemView?.productNameEditText?.text?.toString())?.toInt()

                    }

                }
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    //if (s.length != 0) field2.setText("")
                }
            })


//            itemView?.decreaseImage.setOnClickListener{
//                if (Integer.parseInt(itemView?.increasementText?.text.toString())>1)
//                    itemView?.increasementText?.text = (Integer.parseInt(itemView?.increasementText?.text.toString())-1).toString()
//
//               // listener?.onDecreaseItemClick(adapterPosition, itemView?.increasementText?.text.toString()?:"")
//
//            }
//            itemView?.plusImage.setOnClickListener{
//                    itemView?.increasementText?.text = (Integer.parseInt(itemView?.increasementText?.text.toString())+1).toString()
//               // listener?.onIncreaseItemClick(adapterPosition, itemView?.increasementText?.text.toString()?:"")
//
//
//            }
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
//            adapterPositionChecked = adapterPosition
//            notifyDataSetChanged() // reload this please


            arrayListOfTutorials[position].isChecked = !arrayListOfTutorials[position].isChecked

            if (arrayListOfTutorials[position].isChecked)
            {
                showHideViews(itemView,View.VISIBLE)

            }
            else{
                showHideViews(itemView,View.GONE)

            }


            listener.onClickItemClick(position, quantityList , storesIdList )


            Log.d("Arraysss" , "quantityAdapter ${quantityList.size}")
            Log.d("Arraysss" , "storeAdaapter ${storesIdList.size}")

            // send this item please
            // model.setNotifyItemSelected(arrayListOfTutorials.get(position)?:"") // update sign up fragment please
             //notifyDataSetChanged()


          }

    }
    var adapterPositionChecked = -1 // this is the checked item to use

}