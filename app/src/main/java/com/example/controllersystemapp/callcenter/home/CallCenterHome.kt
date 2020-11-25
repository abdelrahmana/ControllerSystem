package com.example.controllersystemapp.callcenter.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.controllersystemapp.R
import com.example.util.CommonActivity
import com.example.util.NameUtils
import com.example.util.PrefsUtil
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.call_center_home.*

class CallCenterHome : CommonActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_center_home)



        setUserData()


        makeOrderCard?.setOnClickListener(this)
        accountantDelegatesCard?.setOnClickListener(this)

        //setSingleEvent(gridLayout)



        settingAccountant?.setOnClickListener{

            val intent = Intent(this , RedirectCallCenter::class.java)
            intent.putExtra(NameUtils.redirectFragmet, NameUtils.settingsFragmet)
            startActivity(intent)

        }
    }

    private fun setUserData() {

        callCenterName?.text = PrefsUtil.getUserModel(this)?.name?:""

    }

    override fun onResume() {
        super.onResume()
        removeCardSelection()

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.accountantDelegatesCard -> {
                removeCardSelection()
                setCardSelection(accountantDelegatesCard , accountantDelegatesImage , accountantDelegatesText ,R.drawable.ic_accountant_selected)
                //startActivity(Intent(this , AdminAccountantActivity::class.java))
                val intent = Intent(this , RedirectCallCenter::class.java)
              intent.putExtra(NameUtils.redirectFragmet, NameUtils.ACCOUNTANT_DELEAGTES)
               startActivity(intent)
            }

            R.id.makeOrderCard -> {
                removeCardSelection()
                setCardSelection(makeOrderCard , makeOrderImg , makeOrderText ,R.drawable.ic_products_store_selected)
                //startActivity(Intent(this , AdminSpecialCustomersActivity::class.java))
                val intent = Intent(this , RedirectCallCenter::class.java)
                intent.putExtra(NameUtils.redirectFragmet, NameUtils.MAKE_SPECIAL_ORDER)
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



        accountantDelegatesCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        accountantDelegatesImage?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_accountant_icon))
        accountantDelegatesText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        makeOrderCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        makeOrderImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_product_store))
        makeOrderText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))



    }
}