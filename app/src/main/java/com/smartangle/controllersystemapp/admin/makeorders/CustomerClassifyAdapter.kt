package com.smartangle.controllersystemapp.admin.makeorders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantData
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientsData
import kotlinx.android.synthetic.main.responsible_person_item.view.*

class CustomerClassifyAdapter(
    var personList: ArrayList<ClientsData>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener) :
    RecyclerView.Adapter<CustomerClassifyAdapter.ViewHolder>(){



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
            accountantModel: ClientsData,
            onRecyclerItemClickListener: OnRecyclerItemClickListener
        ) {



            if (selctedPosition == adapterPosition)
            {
                itemView.doneImg.visibility = View.VISIBLE
              //  selctedPosition = -1
            }
            else{
                itemView.doneImg.visibility = View.GONE

            }

            itemView.personName.text = accountantModel.name
            itemView.personPhone.text = accountantModel.phone
           // Glide.with(itemView.context!!).load(accountantModel.ima).into(itemView.personImg)


            itemView.setOnClickListener {

                selctedPosition = adapterPosition
                onRecyclerItemClickListener.onItemClick(adapterPosition)
                notifyDataSetChanged()

            }





        }


    }
    var selctedPosition = -1




}