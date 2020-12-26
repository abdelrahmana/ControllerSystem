package com.smartangle.controllersystemapp.admin.settings.admin

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.AccDelegateDetailsBottomSheet
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.EditAccountantRequest
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.bottomsheets.AdminBottomSheet
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils.ADMIN_ID
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_admin.*

class AdminFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var rootView: View
    var adminList = ArrayList<Admin>()
    lateinit var adminAdapter: AdminAdapter

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageAdmin?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }


        addAdminBtn?.setOnClickListener {

            UtilKotlin.changeFragmentBack(
                activity!!,
                AddAdminFragment(), "AddAdmin", null, R.id.frameLayout_direction
            )

        }

        Log.d("dataaa", "create")

    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AdminListResponse) {
                    Log.d("testApi", "isForyou")
                    getAdminListData(datamodel)
                }

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successRemove(datamodel)
                }
                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.stringDataVar.observe(activity!!, Observer { stringData ->

            if (stringData != null) {
                Log.d("dataaa", "observe")

                if (stringData == "remove") {
                    getAdminData()
                }


                model.setStringVar(null)
            }

        })
        model.errorMessage.observe(activity!!, Observer { error ->

            if (error != null) {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })

        model.notifyItemSelected?.observe(activity!!, Observer { datamodel ->

            if (datamodel != null) {


                if (datamodel == 2) {

                    blockAdmin()

                }


                model.setNotifyItemSelected(null) // start details with this data please
            }

        })


    }

    private fun blockAdmin() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            val editAccountantRequest = EditAccountantRequest(
                adminList[selectedItemPosition].id?:0 ,
                adminList[selectedItemPosition].name?:"" ,
                adminList[selectedItemPosition].phone?:"",
                adminList[selectedItemPosition].city_id?.toInt(),
                adminList[selectedItemPosition].email?:"",
                0
            )

            AdminPresenter.getEditAdmin(webService!!, editAccountantRequest, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun successRemove(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            activity?.let {
                UtilKotlin.showSnackMessage(it,successModel?.msg[0])
            }

//            productsAdapter.let {
//                it?.removeItemFromList(removePosition)
//            }
//            productsAdapter?.notifyDataSetChanged()

            getAdminData()

//            model.responseCodeDataSetter(null) // start details with this data please
//            activity?.supportFragmentManager?.popBackStack()

        }




    }

    private fun getAdminListData(adminListResponse: AdminListResponse) {


        if (adminListResponse?.data?.list?.isNullOrEmpty() == false) {
            adminList.clear()
            adminList.addAll(adminListResponse?.data?.list)
            adminAdapter = AdminAdapter(adminList, this)
            adminRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
                adapter = adminAdapter
            }
        } else {
            //empty

        }


    }

    override fun onResume() {
        super.onResume()

        Log.d("dataaa", "resume")

        observeData()

        getAdminData()

    }

    private fun getAdminData() {


        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AdminPresenter.getAdminsList(webService!!, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    var selectedItemPosition = 0


    override fun onItemClick(position: Int) {


        val bundle = Bundle()
        bundle.putInt(ADMINID, adminList[position].id?:0)

        UtilKotlin.changeFragmentBack(activity!! ,
            AdminDetailsFragment() , "AdminDetailsFragment"  , bundle,R.id.frameLayout_direction)



    }

    override fun adminOptionsClickListener(position: Int) {

        Log.d("click", "admin")
        selectedItemPosition = position

        val bundle = Bundle()
        bundle.putInt(ADMIN_ID, adminList.get(position).id ?: 0)
        val adminBottomSheet = AdminBottomSheet.newInstance()
        adminBottomSheet.arguments = bundle
        adminBottomSheet.show(activity?.supportFragmentManager!!, AdminBottomSheet.TAG)

    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)
            it?.stringDataVar?.removeObservers(activity!!)
            it?.notifyItemSelected?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    companion object{

        val ADMINID = "ADMIN_Id"

    }
}