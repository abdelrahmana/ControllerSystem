package com.example.controllersystemapp.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.controllersystemapp.R
import com.example.util.CommonActivity
import com.example.util.NameUtils.categoriesFragmet
import com.example.util.NameUtils.delegatesAccountantsFragmet
import com.example.util.NameUtils.deliveryFragmet
import com.example.util.NameUtils.makeOrderFragmet
import com.example.util.NameUtils.notificationsFragmet
import com.example.util.NameUtils.redirectFragmet
import com.example.util.NameUtils.reporstFragmet
import com.example.util.NameUtils.settingsFragmet
import com.example.util.NameUtils.specialCustomersFragmet
import com.example.util.NameUtils.storeProductsFragmet
import com.example.util.PrefsUtil
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_admin_home.*

class AdminHomeActivity : CommonActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)


        setUserData()


        productsStoreCard?.setOnClickListener(this)
        accountantDelegatCard?.setOnClickListener(this)
        reportsCard?.setOnClickListener(this)
        deliveryCard?.setOnClickListener(this)
        categoriesCard?.setOnClickListener(this)
        customersCard?.setOnClickListener(this)
        makeOrderCard?.setOnClickListener(this)

        //setSingleEvent(gridLayout)


        settingIcon?.setOnClickListener{

            val intent = Intent(this , RedirectFragmentsActivity::class.java)
            intent.putExtra(redirectFragmet , settingsFragmet)
            startActivity(intent)

        }

        notificationIcon?.setOnClickListener{

            val intent = Intent(this , RedirectFragmentsActivity::class.java)
            intent.putExtra(redirectFragmet , notificationsFragmet)
            startActivity(intent)

        }
    }

    private fun setUserData() {

        name?.text = PrefsUtil.getUserModel(this)?.name?:""

    }


    override fun onResume() {
        super.onResume()
        removeCardSelection()

    }




    override fun onClick(v: View?) {

        when(v?.id)
        {
            R.id.productsStoreCard -> {

                removeCardSelection()
                setCardSelection(productsStoreCard , productStoreImg , productStoreImgText ,R.drawable.ic_products_store_selected)
                val intent = Intent(this , RedirectFragmentsActivity::class.java)
                intent.putExtra(redirectFragmet , storeProductsFragmet)
                startActivity(intent)
                //startActivity(Intent(this , AdminStoresProductActivity::class.java))
            }

            R.id.accountantDelegatCard -> {
                removeCardSelection()
                setCardSelection(accountantDelegatCard , accountantDelegatImage , accountantDelegatText ,R.drawable.ic_accountant_selected)
                //startActivity(Intent(this , AdminAccountantActivity::class.java))
                val intent = Intent(this , RedirectFragmentsActivity::class.java)
                intent.putExtra(redirectFragmet , delegatesAccountantsFragmet)
                startActivity(intent)
            }

            R.id.reportsCard -> {
                removeCardSelection()
                setCardSelection(reportsCard , reportsImg , reportsText ,R.drawable.ic_reporst_selected)
                //startActivity(Intent(this , AdminReportsActivity::class.java))
                val intent = Intent(this , RedirectFragmentsActivity::class.java)
                intent.putExtra(redirectFragmet , reporstFragmet)
                startActivity(intent)
            }

            R.id.deliveryCard -> {
                removeCardSelection()
                setCardSelection(deliveryCard , deliveryImg , deliveryText ,R.drawable.ic_delivery_selected)
                //startActivity(Intent(this , AdminDeliveryActivity::class.java))
                val intent = Intent(this , RedirectFragmentsActivity::class.java)
                intent.putExtra(redirectFragmet , deliveryFragmet)
                startActivity(intent)
            }

            R.id.categoriesCard -> {
                removeCardSelection()
                setCardSelection(categoriesCard , categoriesImg , categoryText ,R.drawable.ic_category_selected)
                //startActivity(Intent(this , AdminMakeOrdersActivity::class.java))
                val intent = Intent(this , RedirectFragmentsActivity::class.java)
                intent.putExtra(redirectFragmet , categoriesFragmet)
                startActivity(intent)
            }

            R.id.customersCard -> {
                removeCardSelection()
                setCardSelection(customersCard , customerImg , customerText ,R.drawable.ic_customers_selected)
                //startActivity(Intent(this , AdminSpecialCustomersActivity::class.java))
                val intent = Intent(this , RedirectFragmentsActivity::class.java)
                intent.putExtra(redirectFragmet , specialCustomersFragmet)
                startActivity(intent)
            }

            R.id.makeOrderCard -> {
                removeCardSelection()
                setCardSelection(makeOrderCard , makeOrderImg , makeOrderText ,R.drawable.ic_order_selected)
                //startActivity(Intent(this , AdminSpecialCustomersActivity::class.java))
                val intent = Intent(this , RedirectFragmentsActivity::class.java)
                intent.putExtra(redirectFragmet , makeOrderFragmet)
                startActivity(intent)
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

        categoriesCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        categoriesImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_category))
        categoryText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))

        customersCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        customerImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_customers_icon))
        customerText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))

        makeOrderCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        makeOrderImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_order_icon))
        makeOrderText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))

    }
}