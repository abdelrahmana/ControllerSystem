package com.smartangle.controllersystemapp.accountant.noticesandreports

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantData
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.responsible_person_item.view.*

class SelectDeleagteAdapter(
    var personList: ArrayList<AccountantData>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener,
    val modelData: ViewModelHandleChangeFragmentclass
     ) :
    RecyclerView.Adapter<SelectDeleagteAdapter.ViewHolder>(){

    var selectedDelegatId = ArrayList<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.responsible_person_item , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(personList[position], onRecyclerItemClickListener)


    }


    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            accountantModel: AccountantData,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {



//            if (selctedPosition == adapterPosition)
//            {
//                itemView.doneImg.visibility = View.VISIBLE
//                selctedPosition = -1
//            }
//            else{
//                itemView.doneImg.visibility = View.GONE
//
//            }

            itemView.personName.text = accountantModel.name
            itemView.personPhone.text = accountantModel.phone
//            Glide.with(itemView.context!!).load(accountantModel.image).into(itemView.personImg)


            itemView.setOnClickListener {

                if (itemView.doneImg.visibility == View.VISIBLE)
                {
                    Log.d("visib" , "visible")
                    itemView.doneImg.visibility = View.GONE
                }
                else{
                    itemView.doneImg.visibility = View.VISIBLE
                    selectedDelegatId.add(personList[adapterPosition].id?:-1)
                }
               // Log.d("visib" , "count ${itemView.doneImg.visibility}")

               // modelData.setNotifyItemSelected(selectedDelegatId)
                selctedPosition = adapterPosition
                onRecyclerItemClickListener.onItemClick(adapterPosition)
               // notifyDataSetChanged()

            }





        }


    }

    companion object{

        var selctedPosition = -1

    }


}