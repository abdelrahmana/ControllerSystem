package com.example.controllersystemapp.admin.storeclassification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.productclassification.productsubclassification.FragmentSubProductclassification
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.ProductClassificationAdaptor
import com.photonect.photographerapp.notificationphotographer.DonePackgae.StoredClassificationAdaptor
import kotlinx.android.synthetic.main.fragment_product_classification.*

class StoreClassificationFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var model :ViewModelHandleChangeFragmentclass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = UtilKotlin.declarViewModel(activity!!)!!
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_classification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleViewData()
        setViewModelListener()
        setClickListener()
    }
    var storedAdapter : StoredClassificationAdaptor?=null
    private fun setRecycleViewData() {
        val arrayList = ArrayList<Any>()
        arrayList.add("مخزن 1")
        arrayList.add("مخزن 2")
        arrayList.add("مخزن 3")
        storedAdapter = StoredClassificationAdaptor(model,arrayList)
        UtilKotlin.setRecycleView(productList,storedAdapter!!,
            RecyclerView.VERTICAL,context!!, null, true)
    }

    override fun onDestroyView() {
      model?.notifyItemSelected?.removeObservers(activity!!)
        super.onDestroyView()

    }

    fun setViewModelListener() {
       // please add the item you want to add in arraylist here
       model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
           if (modelSelected != null) { // if null here so it's new service with no any data
               if (modelSelected is Any) {
                   // if (modelSelected.isItCurrent) {
                   // initSlider(modelSelected.pictures)
                   // }
                   model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
                   val bundle = Bundle()
              //     bundle.putInt(EXITENCEIDPACKAGE,availableServiceList.get(position).id?:-1)
                   UtilKotlin.changeFragmentWithBack(activity!! , R.id.container , FragmentSubProductclassification() , bundle)
                   addProductButton?.visibility = View.VISIBLE
                   this.modelSelected = modelSelected
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

    var modelSelected : Any?=null
    private fun setClickListener() {
        addProductButton?.setOnClickListener{
            // modelselected is the model of tasnef ma5sn

        }
    }
}