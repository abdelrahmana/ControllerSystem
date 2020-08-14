package com.example.controllersystemapp.admin.storesproducts.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.controllersystemapp.ModelStringID
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AddAccountantFragment
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_add_store.*


class AddStoreFragment : Fragment() {


    lateinit var model : ViewModelHandleChangeFragmentclass
    lateinit var rootView: View
    var personName = ""
    var persomId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_store, container, false)
        model =UtilKotlin.declarViewModel(activity!!)!!
//        activity?.run {
//            model = ViewModelProviders.of(activity!!).get(ViewModelHandleChangeFragmentclass::class.java)
//        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        responsiblePersonContainer?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! , ResponsiblePersonFragment() , "ResponsiblePerson"  , null,R.id.frameLayout_direction)

        }

        backImgAddStore?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        increaseCountBtn?.setOnClickListener {

            val count = (counter?.text.toString()).toInt()
            counter?.text = (count + 1).toString()

        }

        decreaseCountBtn?.setOnClickListener {

            val count = (counter?.text.toString()).toInt()

            if (count > 0)
            {
                counter?.text = (count - 1).toString()

            }
            else{
                counter?.text = (0).toString()

            }



        }

        Log.d("model" , "create")
        Log.d("model" , "createName $personName")

        model.stringNameData.observe(activity!! ,
            Observer<ModelStringID> { modeStringId ->
                Log.d("model" , "dataaa")

                if (modeStringId !=null) {

                    personName = modeStringId.name
                    //setData(personName)
                    Log.d("model" , "name ${modeStringId.name}")
                    Log.d("model" , "id ${modeStringId.id}")
                }

            })

    }

    private fun setData(personName: String) {
        Log.d("model" , "nnnn")
        Log.d("model" , "var $personName")



    }

    override fun onResume() {
        super.onResume()
        Log.d("model" , "resume")
        responsiblePersonEditText?.setText(personName)

    }

    override fun onDestroyView() {
        model.let {
            it.stringNameData.removeObservers(activity!!)

        }
        super.onDestroyView()

    }

}