package com.photonect.photographerapp.notificationphotographer.DonePackgae

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.products.models.Data
import com.example.controllersystemapp.admin.interfaces.OnStoreSelcteClickListener
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.acc_product_select_item.view.*


class AccountantProductsListAdapter(
    val modelData: ViewModelHandleChangeFragmentclass,
    val delegateProductsslist: ArrayList<Data>
    //this method is returning the view for each item in the list
    //, val listener: OnStoreSelcteClickListener
) : RecyclerView.Adapter<AccountantProductsListAdapter.ViewHolder>() {

    var quantityList = ArrayList<Int>()
    var storesIdList = ArrayList<Int>()
    var clickFlag = false
    var clickRowFlag = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.acc_product_select_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(delegateProductsslist[position],/*modelData*/delegateProductsslist)
        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return delegateProductsslist.size
    }

//    fun getSelected(): ArrayList<Data>? {
//        val selected: ArrayList<Data> = ArrayList()
//        for (i in 0 until delegateProductsslist.size) {
//            if (delegateProductsslist.get(i).isChecked!!) {
//                selected.add(delegateProductsslist[i])
//            }
//        }
//        return selected
//    }

    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(
            itemData: Data,
            /*model: ViewModelData,*/
            arrayListOffersValues: ArrayList<Data>
        ) {

            Glide.with(itemView.context).load(itemData.image ?: "")
//                .placeholder(R.drawable.no_profile)
//                .error(R.drawable.no_profile)
                .into(itemView.productImage)
            itemView.store_name.text = itemData?.name ?: ""
            itemView.totalQuantityTxt.text =
                "${itemData?.total_quantity ?: ""} ${itemView?.context?.getString(R.string.piece)}"

            if (adapterPositionChecked == adapterPosition) {
                showHideViews(itemView, View.VISIBLE)
            } else {
                showHideViews(itemView, View.GONE)
            }

            itemView?.productNameEditText?.setText((itemData.selectedQuantity ?: 1).toString())
            itemView?.productNameEditText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {

                    if (s.length != 0) {

                        // modelData.setNotifyItemSelected()
                        itemData.selectedQuantity =
                            (itemView?.productNameEditText?.text?.toString())?.toInt()
                        modelData.setNotifyItemSelected(
                                delegateProductsslist.get(adapterPosition) ?: ""
                            )
//                        if ((itemView?.productNameEditText?.text?.toString())?.toInt() ?: 0 > (itemData.total_quantity?.toInt()) ?: 0)
//                            modelData.setNotifyItemSelected("error") // update sign up fragment please
//                        else
//                        {
//                            itemData.selectedQuantity =
//                                (itemView?.productNameEditText?.text?.toString())?.toInt()
//                            modelData.setNotifyItemSelected(
//                                delegateProductsslist.get(adapterPosition) ?: ""
//                            )
//                        }



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
            itemView.itemContainer.setOnClickListener {

                onItemClicked(modelData, adapterPosition) // go to details please
            }


        }

        private fun showHideViews(itemView: View, showGone: Int) {
            Log.d("pos", "showw")

            itemView?.checked?.visibility = showGone
            itemView?.containerNumber.visibility = showGone
        }


        private fun onItemClicked(model: ViewModelHandleChangeFragmentclass, position: Int) {
            Log.d("pos", "$position")
            adapterPositionChecked = adapterPosition
            model.setNotifyItemSelected(
                delegateProductsslist.get(position) ?: ""
            ) // update sign up fragment please
            notifyDataSetChanged() // reload this please

        }

    }

    var adapterPositionChecked = -1 // this is the checked item to use

}