package com.example.controllersystemapp.callcenter.responsibledelegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantData
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.responsible_person_item.view.*

class ResponsibleDelegateAdapter(
    var personList: ArrayList<CallCenterDelegateData>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<ResponsibleDelegateAdapter.ViewHolder>(){



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

    fun updateData(newList: List<CallCenterDelegateData>?) {
        val start = itemCount
        if (newList != null) {
            if (!newList.isEmpty()) { // if this is the first time
                if (personList.size > 0) // has old data
                {
                    this.personList.addAll(newList)
                    notifyItemRangeInserted(start, personList.size - 1)
                } else {
                    this.personList.addAll(newList)
                    notifyDataSetChanged()
                }

            }
        }
    }
    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            accountantModel: CallCenterDelegateData,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {



            if (selctedPosition == adapterPosition)
            {
                itemView.doneImg.visibility = View.VISIBLE
                selctedPosition = -1
            }
            else{
                itemView.doneImg.visibility = View.GONE

            }

            itemView.personName.text = accountantModel.name
            itemView.personPhone.text = accountantModel.phone
            Glide.with(itemView.context!!).load(accountantModel.image).into(itemView.personImg)


            itemView.setOnClickListener {
                selctedPosition = adapterPosition
                onRecyclerItemClickListener.onItemClick(adapterPosition)
                notifyDataSetChanged()

            }





        }


    }

    companion object{

        var selctedPosition = -1

    }


}