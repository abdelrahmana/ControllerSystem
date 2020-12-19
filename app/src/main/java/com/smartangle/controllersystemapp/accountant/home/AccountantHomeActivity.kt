package com.smartangle.controllersystemapp.accountant.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.smartangle.controllersystemapp.R
import com.smartangle.util.CommonActivity
import com.smartangle.util.NameUtils
import com.smartangle.util.PrefsUtil
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_accountant_home.*

class AccountantHomeActivity : CommonActivity(), View.OnClickListener {
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
        accountantCreateDebts?.setOnClickListener(this)

        //setSingleEvent(gridLayout)


        settingAccountant?.setOnClickListener{

            val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
            intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.ACCOUNTANT_SETTINGS)
            startActivity(intent)

        }

        notificationAccountant?.setOnClickListener{

            val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
            intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.notificationsFragmet)
            startActivity(intent)

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
              intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.ACCOUNTANT_DELEAGTES)
               startActivity(intent)
            }

            R.id.accountantNoticesCard -> {
                removeCardSelection()
                setCardSelection(accountantNoticesCard , accountantNoticesImg , accountantNoticesText ,R.drawable.notices_reports_select)
                //startActivity(Intent(this , AdminReportsActivity::class.java))
                val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
                intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.NOTICES_AND_REPORTS)
                startActivity(intent)
            }

            R.id.accountantSpecialOrderCard -> {
                removeCardSelection()
                setCardSelection(accountantSpecialOrderCard , accountantSpecialOrderImg , accountantSpecialOrderText ,R.drawable.ic_order_selected)
                //startActivity(Intent(this , AdminDeliveryActivity::class.java))
                val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
                intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.MAKE_SPECIAL_ORDER)
                startActivity(intent)
            }

            R.id.sendSalesCard -> {
                removeCardSelection()
                setCardSelection(sendSalesCard , sendSalesImg , sendSalesText ,R.drawable.ic_send_sales_unselect)

                val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
                intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.SEND_SALES)
                startActivity(intent)
            }

            R.id.accountantDeliveryCard -> {
                removeCardSelection()
                setCardSelection(accountantDeliveryCard , accountantDeliveryImg , accountantDeliveryText ,R.drawable.ic_delivery_selected)
                val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
                intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.DELIVERYSETTING)
                startActivity(intent)
            }

            R.id.accountantCreateDebts -> {
                removeCardSelection()
                setCardSelection(accountantCreateDebts , accountantCreateDebtsImg , accountantCreateDebtsText ,R.drawable.ic_delivery_selected)
                val intent = Intent(this , RedirectAccountantsFragmentActivity::class.java)
                intent.putExtra(NameUtils.redirectAccFragmet, NameUtils.CREATE_DEBTS)
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

        accountantCreateDebts?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        accountantCreateDebtsImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_delivery))
        accountantCreateDebtsText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


    }
}