package com.example.controllersystemapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.AdminAccountantActivity
import com.example.controllersystemapp.admin.delivery.AdminDeliveryActivity
import com.example.controllersystemapp.admin.makeorders.AdminMakeOrdersActivity
import com.example.controllersystemapp.admin.reports.AdminReportsActivity
import com.example.controllersystemapp.admin.specialcustomers.AdminSpecialCustomersActivity
import com.example.controllersystemapp.admin.storesproducts.AdminStoresProductActivity
import com.example.util.CommonActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_admin_home.*

class AdminHomeActivity : CommonActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        productsStoreCard?.setOnClickListener(this)
        accountantDelegatCard?.setOnClickListener(this)
        reportsCard?.setOnClickListener(this)
        deliveryCard?.setOnClickListener(this)
        makeOrderCard?.setOnClickListener(this)
        customersCard?.setOnClickListener(this)

        //setSingleEvent(gridLayout)

        notificationIcon?.setOnClickListener{
            Log.d("CLick" , "Notification")
        }
        settingIcon?.setOnClickListener{
            Log.d("CLick" , "Settings")
        }
    }


    override fun onClick(v: View?) {

        when(v?.id)
        {
            R.id.productsStoreCard -> {

                removeCardSelection()
                setCardSelection(productsStoreCard , productStoreImg , productStoreImgText ,R.drawable.ic_products_store_selected)
                startActivity(Intent(this , AdminStoresProductActivity::class.java))
            }

            R.id.accountantDelegatCard -> {
                removeCardSelection()
                setCardSelection(accountantDelegatCard , accountantDelegatImage , accountantDelegatText ,R.drawable.ic_accountant_selected)
                startActivity(Intent(this , AdminAccountantActivity::class.java))
            }

            R.id.reportsCard -> {
                removeCardSelection()
                setCardSelection(reportsCard , reportsImg , reportsText ,R.drawable.ic_reporst_selected)
                startActivity(Intent(this , AdminReportsActivity::class.java))
            }

            R.id.deliveryCard -> {
                removeCardSelection()
                setCardSelection(deliveryCard , deliveryImg , deliveryText ,R.drawable.ic_delivery_selected)
                startActivity(Intent(this , AdminDeliveryActivity::class.java))
            }

            R.id.makeOrderCard -> {
                removeCardSelection()
                setCardSelection(makeOrderCard , makeOrderImg , makeOrderText ,R.drawable.ic_order_selected)
                startActivity(Intent(this , AdminMakeOrdersActivity::class.java))
            }

            R.id.customersCard -> {
                removeCardSelection()
                setCardSelection(customersCard , customerImg , customerText ,R.drawable.ic_customers_selected)
                startActivity(Intent(this , AdminSpecialCustomersActivity::class.java))
            }

        }


    }

    private fun setCardSelection(card: MaterialCardView?, imageView: ImageView?, textView: TextView?, icImage: Int) {
        imageView?.setImageDrawable(ContextCompat.getDrawable(this , icImage))
        textView?.setTextColor(ContextCompat.getColor(this , R.color.white))
        card?.setBackgroundColor(ContextCompat.getColor(this , R.color.selctedOrange))
    }

    private fun removeCardSelection() {


        productsStoreCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        productStoreImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_product_store))
        productStoreImgText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        accountantDelegatCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        accountantDelegatImage?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_accountant_icon))
        accountantDelegatText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        reportsCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        reportsImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_reports_icon))
        reportsText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))

        deliveryCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        deliveryImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_delivery))
        deliveryText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))

        makeOrderCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        makeOrderImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_order_icon))
        makeOrderText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))

        customersCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        customerImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_customers_icon))
        customerText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


    }
}