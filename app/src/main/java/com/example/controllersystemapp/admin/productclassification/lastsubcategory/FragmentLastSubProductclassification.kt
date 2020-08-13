package com.example.controllersystemapp.admin.productclassification.lastsubcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.util.NameUtils.productId
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_product_classification.*

class FragmentLastSubProductclassification : Fragment() {


    lateinit var model : ViewModelHandleChangeFragmentclass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model =UtilKotlin.declarViewModel(activity!!)!!
        return inflater.inflate(R.layout.fragment_product_classification, container, false)
    }

    var subProductAdaptor : LastSubProductClassificationAdaptor?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subCategroyHeader?.text = arguments?.getString(productId)?:"الكترونيات"
           setRecycleViewData() // set recycleView
        setViewModelListener() // last item clicked now we need to go to the add product fragment and no back please
        activity!!.supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        unSubscribeListners()
        super.onDestroyView()
    }
    fun unSubscribeListners() {
        model.let {
            // not from all listner
            it?.responseDataCode?.removeObservers(activity!!) // remove observer from here only
            it?.notifyItemSelected?.removeObservers(activity!!)

        }

    }
    private fun setRecycleViewData() {
        val arrayList = ArrayList<Any>()
        arrayList.add("العاب")
        arrayList.add("منزل")
        arrayList.add("اجهزة منزلية")
        subProductAdaptor = LastSubProductClassificationAdaptor(model,arrayList)
        UtilKotlin.setRecycleView(productList,subProductAdaptor!!,
            RecyclerView.VERTICAL,context!!, null, true)
    }

    fun setViewModelListener() {
        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Any) { // dont set this to null because this will go to the first page
                    // if (modelSelected.isItCurrent) {
                    // initSlider(modelSelected.pictures)
                    // }
            /*        model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
                    val bundle = Bundle()
                    //     bundle.putInt(EXITENCEIDPACKAGE,availableServiceList.get(position).id?:-1)
                    UtilKotlin.changeFragmentWithBack(activity!! , R.id.container , FragmentLastSubProductclassification() , bundle)*/

                    setAddButton()
                }
                /* else if (modelSelected is ImageModelData) // if it is object of this model
                  {
                      /*  val pictures = ArrayList<Picture>()
                        datamodel.image?.forEach{
                            pictures.add(Picture(it))

                        }*/

                      //  initSlider(pictures) // add these services to image
                      //getData(datamodel) // move data to here please
                  }
  */
            }
        })
    }

    private fun setAddButton() {
        addProductButton?.visibility = View.VISIBLE
        addProductButton?.setOnClickListener{
          /* val index = activity!!.supportFragmentManager.backStackEntryCount - 1
            val lastEntry: FragmentManager.BackStackEntry =
                activity!!.supportFragmentManager!!.getBackStackEntryAt(index)
            val previous: FragmentManager.BackStackEntry =
                activity!!.supportFragmentManager!!.getBackStackEntryAt(index - 3)
            activity!!.supportFragmentManager.popBackStack(previous.id,0)*/

            activity!!.supportFragmentManager.popBackStack()
            activity!!.supportFragmentManager.popBackStack()
            activity!!.supportFragmentManager.popBackStack()

        }
    }
}