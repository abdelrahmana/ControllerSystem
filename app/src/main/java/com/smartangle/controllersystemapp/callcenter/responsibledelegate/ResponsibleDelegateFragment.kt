package com.smartangle.controllersystemapp.callcenter.responsibledelegate

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.ModelStringID
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.callcenter.delegate.DelegatePresenter
import com.smartangle.controllersystemapp.callcenter.delegate.model.DelegateResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NestedScrollPaginationView
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_responsible_person.*
import kotlinx.android.synthetic.main.fragment_responsible_person.searchIcon
import kotlinx.android.synthetic.main.search_layout.*
import retrofit2.Response


class ResponsibleDelegateFragment : Fragment() , OnRecyclerItemClickListener,
    NestedScrollPaginationView.OnMyScrollChangeListener {

    lateinit var model : ViewModelHandleChangeFragmentclass
    lateinit var rootView: View

    var webService: WebService? = null
    lateinit var progressDialog : Dialog


    lateinit var responsiblePersonAdapter: ResponsibleDelegateAdapter
    var personList = ArrayList<CallCenterDelegateData>()

    var personName = ""
    var personId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // model = UtilKotlin.declarViewModel(this)!!

        rootView = inflater.inflate(R.layout.fragment_responsible_person, container, false)

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

//        activity?.run {
//            model = ViewModelProviders.of(activity!!).get(ViewModelHandleChangeFragmentclass::class.java)
//        }
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

        textView3?.text = getString(R.string.delegate_choose)
        searchIcon?.setOnClickListener{
            renitializePages()
            currentSearchText= searchEditText?.text.toString()
            getDelegatesList()
        }
      setResponsibleAdapter()
        getDelegatesList()

    }

    private fun setResponsibleAdapter() {
        responsiblePersonAdapter = ResponsibleDelegateAdapter (personList , this)
        responsiblePersonRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = responsiblePersonAdapter
        }
    }

    private fun renitializePages() {
        scrollingNestedScroll?.resetPageCounter() // to 1
        page = 1
        personList.clear()

    }
    var currentSearchText = ""
    var page = 1
    var hasMorePages = false

    private fun getDelegatesList() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val hashMap = HashMap<String,Any>()
            hashMap.put("page",page)
            if (currentSearchText.isNotEmpty())
                hashMap.put("name",currentSearchText)
            DelegatePresenter.getDelegatesList(webService!!, delegateObserver(),hashMap)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

        /*    modelHandleChangeFragmentclass.notifyItemSelected?.observe(activity!!, Observer { datamodel ->

                if (datamodel != null) {

                    if (datamodel ==1) {

                        val bundle = Bundle()
                        bundle.putString(NameUtils.CURRENT_DELEGATE, Gson().toJson(delegatesList.get(selectedItemPosition)))
                        UtilKotlin.changeFragmentBack(activity!! ,
                            OrdersFragment(),"delegate"  , bundle,
                            arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)


                    }

                    modelHandleChangeFragmentclass.setNotifyItemSelected(null) // start details with this data please
                }

            })*/

    }

    override fun onResume() {
        super.onResume()

        //getPersonList()

    }

    var disposableObserver : DisposableObserver<Response<DelegateResponse>>?=null
    private fun delegateObserver(): DisposableObserver<Response<DelegateResponse>> {

        disposableObserver= object : DisposableObserver<Response<DelegateResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<DelegateResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    // delegatesList.clear()
                    hasMorePages = response.body()?.data?.has_more_page?:false // default false
                    // delegatesList.addAll(response.body()?.data?.list?:ArrayList())
                    responsiblePersonAdapter.updateData(response.body()?.data?.list?:ArrayList())
                }
                else
                {
                    progressDialog?.dismiss()
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
        return disposableObserver!!
    }

/*    private fun getPersonList(callCenter: CallCenterResponse) {

        if (callCenter?.data?.list?.isNullOrEmpty() == false)
        {
            personList.clear()
            personList.addAll(callCenter?.data?.list?:ArrayList())
            responsiblePersonAdapter = ResponsibleDelegateAdapter (personList , this)
            responsiblePersonRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = responsiblePersonAdapter
            }



        }
        else{

        }




    }*/

    override fun onItemClick(position: Int) {

        personName = personList[position].name!!
        personId = personList[position].id!!


    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    override fun onLoadMore(currentPage: Int) {
        page = currentPage
        if (hasMorePages) {
            getDelegatesList()

        }
    }
}