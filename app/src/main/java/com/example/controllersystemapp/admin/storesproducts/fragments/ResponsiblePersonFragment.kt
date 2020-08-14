package com.example.controllersystemapp.admin.storesproducts.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.ModelStringID
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.AccountantAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantModel
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.admin.storesproducts.adapters.ResponsiblePersonAdapter
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_accountant.*
import kotlinx.android.synthetic.main.fragment_responsible_person.*


class ResponsiblePersonFragment : Fragment() , OnRecyclerItemClickListener {

    lateinit var model : ViewModelHandleChangeFragmentclass
    lateinit var rootView: View

    lateinit var responsiblePersonAdapter: ResponsiblePersonAdapter
    var personList = ArrayList<AccountantModel>()

    var personName = ""
    var personId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // model = UtilKotlin.declarViewModel(this)!!

        rootView = inflater.inflate(R.layout.fragment_responsible_person, container, false)

        activity?.run {
            model = ViewModelProviders.of(activity!!).get(ViewModelHandleChangeFragmentclass::class.java)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        confirmResponsiblePersonBtn?.setOnClickListener {
            model.setStringData(ModelStringID(personName , personId))
            activity?.supportFragmentManager?.popBackStack()
        }

        Log.d("model" , "respo")
       // changeString()
    }

    override fun onResume() {
        super.onResume()

        getPersonList()

    }

    private fun getPersonList() {

        personList.clear()
        personList.add(AccountantModel("احمد حازم" , null , " +966 56784 9876" , 1))
        personList.add(AccountantModel("اعمرو صالح" , null , " +966 56784 9876" , 2))
        personList.add(AccountantModel("مسعد بن زياد" , null , " +966 56784 9876" , 3))
        personList.add(AccountantModel("اطاهر مروان" , null , " +966 56784 9876" , 4))
        personList.add(AccountantModel("ايمن مسعود" , null , "ا +966 56784 9876" , 5))
        personList.add(AccountantModel("فيصل الرابحي" , null , " +966 56784 9876" , 6))


        responsiblePersonAdapter = ResponsiblePersonAdapter (personList , this)
        responsiblePersonRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = responsiblePersonAdapter
        }


    }

    override fun onItemClick(position: Int) {

        Log.d("clickRespon" , "$position")
        personName = personList[position].name!!
        personId = personList[position].Id!!


    }
}