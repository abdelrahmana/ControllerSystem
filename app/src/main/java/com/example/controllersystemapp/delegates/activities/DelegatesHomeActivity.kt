package com.example.controllersystemapp.delegates.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.activity_delegates_home.*


class DelegatesHomeActivity : CommonActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delegates_home)

        setUserData()

        specialWalletCard?.setOnClickListener(this)
        delegateOrdersCard?.setOnClickListener(this)
        notificationReportsCard?.setOnClickListener(this)
        makeOrderDelegateCard?.setOnClickListener(this)


        //setSingleEvent(gridLayout)


        settingIcon?.setOnClickListener{

//            val intent = Intent(this , RedirectFragmentsActivity::class.java)
//            intent.putExtra(NameUtils.redirectFragmet, NameUtils.settingsFragmet)
//            startActivity(intent)

        }

        notificationIcon?.setOnClickListener{

//            val intent = Intent(this , RedirectFragmentsActivity::class.java)
//            intent.putExtra(NameUtils.redirectFragmet, NameUtils.notificationsFragmet)
//            startActivity(intent)

        }

    }

    private fun setUserData() {

        deleagteName?.text = PrefsUtil.getUserModel(this)?.name?:""

    }

    override fun onResume() {
        super.onResume()
        removeCardSelection()

    }

    override fun onClick(v: View?) {

        when(v?.id)
        {
            R.id.specialWalletCard -> {

                removeCardSelection()
                setCardSelection(specialWalletCard , specialWalletImg , specialWalletText ,R.drawable.ic_delivery_selected)
                val intent = Intent(this , RedirecteDelegateActivity::class.java)
                intent.putExtra(NameUtils.redirectDelegFragmet, NameUtils.SEPCIAL_WALLET)
                startActivity(intent)
            }

            R.id.delegateOrdersCard -> {
                removeCardSelection()
                setCardSelection(delegateOrdersCard , delegateOrdersImage , delegateOrdersText ,R.drawable.ic_ordersicon)
                val intent = Intent(this , RedirecteDelegateActivity::class.java)
                intent.putExtra(NameUtils.redirectDelegFragmet, NameUtils.DELEGATE_ORDERS)
                startActivity(intent)
            }

            R.id.notificationReportsCard -> {
                removeCardSelection()
                setCardSelection(notificationReportsCard , notificationReportsImg , notificationReportsText ,R.drawable.ic_reporst_selected)
                val intent = Intent(this , RedirecteDelegateActivity::class.java)
                intent.putExtra(NameUtils.redirectDelegFragmet, NameUtils.DELEGATE_NoTIFY_REPORTS)
                startActivity(intent)
            }

            R.id.makeOrderDelegateCard -> {
                removeCardSelection()
                setCardSelection(makeOrderDelegateCard , makeOrderDelegateImg , makeOrderDelegateText ,R.drawable.ic_order_selected)
                val intent = Intent(this , RedirecteDelegateActivity::class.java)
                intent.putExtra(NameUtils.redirectDelegFragmet, NameUtils.DELEGATE_MAKE_ORDER)
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


        specialWalletCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        specialWalletImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_delivery))
        specialWalletText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        delegateOrdersCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        delegateOrdersImage?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_ordersicon))
        delegateOrdersText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))


        notificationReportsCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        notificationReportsImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_reports_icon))
        notificationReportsText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))

        makeOrderDelegateCard?.setBackgroundColor(ContextCompat.getColor(this , R.color.white))
        makeOrderDelegateImg?.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_order_icon))
        makeOrderDelegateText?.setTextColor(ContextCompat.getColor(this , R.color.textColor))



    }
}