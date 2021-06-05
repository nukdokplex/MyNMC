package ru.nukdotcom.mynmc.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import ru.nukdotcom.mynmc.R
import ru.nukdotcom.mynmc.helpers.MyNMCWebAPI
import ru.nukdotcom.mynmc.helpers.RetrofitBuilder
import ru.nukdotcom.mynmc.helpers.ScheduleModelsRecyclerViewAdapter
import ru.nukdotcom.mynmc.models.ModelType
import ru.nukdotcom.mynmc.models.schedule_model.Model

private const val ARG_MODEL_TYPE = "model_type"

class ScheduleModelsFragment : Fragment() {
    private var model_type: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleModelsRecyclerViewAdapter
    private lateinit var srl: SwipeRefreshLayout
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            model_type = it.getString(ARG_MODEL_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_models, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        srl = view.findViewById(R.id.modelsSwipeRefreshLayout)
        recyclerView = view.findViewById(R.id.modelsRecyclerView)
        adapter = ScheduleModelsRecyclerViewAdapter(arrayListOf(), model_type!!)
        val layoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        srl.setOnRefreshListener {
            refreshModels()
        }
        retrofit = RetrofitBuilder.buildDefaultRetrofit(view.context)
        refreshModels()
    }

    public fun refreshModels(){
        srl.isRefreshing = true
        val api = retrofit.create(MyNMCWebAPI::class.java)
        val modelType = when (model_type){
            "group" -> "groups"
            "teacher" -> "teachers"
            "auditory" -> "auditories"
            else -> ""
        }
        api.getModels(modelType).enqueue(object: Callback<ArrayList<Model>>{
            override fun onResponse(call: Call<ArrayList<Model>>, response: Response<ArrayList<Model>>) {
                if (response.isSuccessful){
                    if (response.body() == null){
                        Toast.makeText(
                            this@ScheduleModelsFragment.requireContext(),
                            R.string.error_unexpected_response,
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("ScheduleModelsFragment", "Unexpected empty response!")
                        srl.isRefreshing = false
                        return
                    }

                    adapter.addItems(response.body()!!)
                }
                else {
                    Toast.makeText(
                        this@ScheduleModelsFragment.requireContext(),
                        String.format(
                            getString(R.string.error_response_error),
                            response.code(),
                            response.message()
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("ScheduleModelsFragment", response.message() + " (" + response.code()+ ") on \"" + call.request().url().toString() + "\"!")
                }
                srl.isRefreshing = false
            }

            override fun onFailure(call: Call<ArrayList<Model>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@ScheduleModelsFragment.requireContext(),
                    R.string.error_request_failed,
                    Toast.LENGTH_LONG
                ).show()
                srl.isRefreshing = false
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(model_type: String) =
            ScheduleModelsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MODEL_TYPE, model_type)
                }
            }
    }
}