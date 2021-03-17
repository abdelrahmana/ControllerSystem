package com.smartangle.controllersystemapp.common.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.common.chat.model.Message
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.adaptor_chat_item.view.*

class ChatAdapter(
                  val arrayList: ArrayList<Message>
                    //this method is returning the view for each item in the list, selectedId: kotlin.Int){}
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adaptor_chat_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position],/*modelData*/arrayList)
        //  setAnimation(holder.itemView, position)


    }
    fun updateData(newList: List<Message>?) {
        val start = itemCount
        if (newList != null) {
            if (!newList.isEmpty()) { // if this is the first time
                if (arrayList.size > 0) // has old data
                {
                    this.arrayList.addAll(newList)
                    notifyItemRangeInserted(start, arrayList.size - 1)
                } else {
                    this.arrayList.addAll(newList)
                    notifyDataSetChanged()
                }

            }
        }
    }
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrayList.size
    }



    //the class is hodling the list view
    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(
                itemData: Message,
            /*model: ViewModelData,*/
                arrayListOffersValues: ArrayList<Message>
        ) {
            itemView.message.text = itemData.message?:""

            if (itemData.type!="sender") // not me
            {
                itemView.recieverImage.visibility = View.VISIBLE
                Glide.with(itemView.context).load(itemData.sender?.image)
                    .error(R.drawable.ic_username).into(itemView.recieverImage)
                itemView.matrialMessage?.setCardBackgroundColor(ContextCompat.getColor( itemView.context , R.color.gray))
                itemView.message?.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
            }

        }


          private fun onItemClicked(model: ViewModelHandleChangeFragmentclass,position: Int) {
              // send this item please
          //    model.setNotifiedItemSelected(arrayListOfClinicServices.get(position)) // update sign up fragment please


          }

    }
    var defaultSelectedItem = 0


}