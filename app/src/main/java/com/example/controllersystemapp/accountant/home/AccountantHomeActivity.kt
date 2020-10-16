package com.example.controllersystemapp.accountant.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.RedirectFragmentsActivity
import com.example.util.NameUtils
import com.example.util.PrefsUtil
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_accountant_home.*

class AccountantHomeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountant_home)



        setUserData()


        accountantProductsCard?.setOnClickListener(this)
        accountantDelegatesCard?.setOnClickListener(this)
        accountantNoticesCard?.setOnClickListener(this)
        accountantSpecialOrderCard?.setOnClickListener(this)
        sendSalesCard?.setOnClickListener(this)
        accountantDeliveryCard?.setOnClickListener(this)

        //setSingleEvent(gridLayout)


        settingAccountant?.setOnClickListener{

//            val intent = Intent(this , RedirectFragmentsActivity::class.java)
//            intent.putExtra(NameUtils.redirectFragmet, NameUtils.settingsFragmet)
//            startActivity(intent)

        }

        notificationAccountant?.setOnClickListener{

//            val intent = Intent(this , RedirectFragmentsActivity::class.java)
//            intent.putExtra(NameUtils.redirectFragmet, NameUtils.notificationsFragmet)
//            startActivity(intent)

        }
    }

    private fun setUserData() {

        accountantName?.text = PrefsUtil.getUserModel(this)?.name?:""

    }

    override fun onResume() {
        super.onResume()
        removeCardSelection()

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.accountantProductsCard -> {

                removeCardSelection()
                setCardSelection(accountantProductsCard , accountantProductImg , accountantProductText
                    ,R.drawable.ic_products_store_selected)

                val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
                intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.ACCOUNTANTS_PRODUCTS)
                startActivity(intent)
                //startActivity(Intent(this , AdminStoresProductActivity::class.java))
            }

            R.id.accountantDelegatesCard -> {
                removeCardSelection()
                setCardSelection(accountantDelegatesCard , accountantDelegatesImage , accountantDelegatesText ,R.drawable.ic_accountant_selected)
                //startActivity(Intent(this , AdminAccountantActivity::class.java))
                val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
              intent.putExtra(NameUtils.redirectFragmet, NameUtils.delegatesAccountantsFragmet)
               startActivity(intent)
            }

            R.id.accountantNoticesCard -> {
                removeCardSelection()
                setCardSelection(accountantNoticesCard , accountantNoticesImg , accountantNoticesText ,R.drawable.notices_reports_select)
                //startActivity(Intent(this , AdminReportsActivity::class.java))
//                val intent = Intent(this , RedirectFragmentsActivity::class.java)
//                intent.putExtra(NameUtils.redirectFragmet, NameUtils.reporstFragmet)
//                startActivity(intent)
            }

            R.id.accountantSpecialOrderCard -> {
                removeCardSelection()
                setCardSelection(accountantSpecialOrderCard , accountantSpecialOrderImg , accountantSpecialOrderText ,R.drawable.ic_order_selected)
                //startActivity(Intent(this , AdminDeliveryActivity::class.java))
//                val intent = Intent(this , RedirectFragmentsActivity::class.java)
//                intent.putExtra(NameUtils.redirectFragmet, NameUtils.deliveryFragmet)
//                startActivity(intent)
            }

            R.id.sendSalesCard -> {
                removeCardSelection()
                setCardSelection(sendSalesCard , sendSalesImg , sendSalesText ,R.drawable.ic_send_sales_unselect)
                //startActivity(Intent(this , AdminMakeOrdersActivity::class.java))
//                val intent = Intent(this , RedirectFragmentsActivity::class.java)
//                intent.putExtra(NameUtils.redirectFragmet, NameUtils.categoriesFragmet)
//                startActivity(intent)
            }

            R.id.accountantDeliveryCard -> {
                removeCardSelection()
                setCardSelection(accountantDeliveryCard , accountantDeliveryImg , accountantDeliveryText ,R.drawable.ic_delivery_selected)
                //startActivity(Intent(this , AdminSpecialCustomersActivity::class.java))
//                val intent = Intent(this , RedirectFragmentsActivity::class.java)
//                intent.putExtra(NameUtils.redirectFragmet, NameUtils.specialCustomersFragmet)
//                startActivity(intent)
            }


        }

    }




    private fun setCardSelection(card: MaterialCardView?, imageView: ImageView?, textView: TextView?, icImage: Int) {
        imageView?.setImageDrawable(ContextCompat.getDrawable(this , icImage))
        textView?.setTextColor(ContextCompat.getColor(this , R.color.white))
        card?.setBackgroundColor(ContextCompat.getColor(this , R.color.selctedOrange))
    }

    private fun removeCardSelection() {

        accountantProductsCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        accountantProductImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_product_store))
        accountantProductText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        accountantDelegatesCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        accountantDelegatesImage?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_accountant_icon))
        accountantDelegatesText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        accountantNoticesCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        accountantNoticesImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.notices_reports_unselect))
        accountantNoticesText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        accountantSpecialOrderCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        accountantSpecialOrderImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_order_icon))
        accountantSpecialOrderText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        sendSalesCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        sendSalesImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_send_sales_unselect))
        sendSalesText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        accountantDeliveryCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        accountantDeliveryImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_delivery))
        accountantDeliveryText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


    }
}