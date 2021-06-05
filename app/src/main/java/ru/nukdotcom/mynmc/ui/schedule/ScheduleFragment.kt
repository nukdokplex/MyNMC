package ru.nukdotcom.mynmc.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.nukdotcom.mynmc.R
import ru.nukdotcom.mynmc.models.ModelType

class ScheduleFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    lateinit var adapter: ScheduleModelTypesViewPager2Adapter
    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager2 = view.findViewById(R.id.modelTypesViewPager2)
        tabLayout = view.findViewById(R.id.modelTypesTabLayout)
        adapter = ScheduleModelTypesViewPager2Adapter(
            arrayListOf<ModelType>(
                ModelType("group", "Группы"),
                ModelType("teacher", "Преподаватели"),
                ModelType("auditory", "Аудитории")
            ),
            this
        )
        viewPager2.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = adapter.modelTypes[position].nicename!!
        }.attach()

    }

    companion object {
        @JvmStatic
        fun newInstance() = ScheduleFragment()
    }
}

class ScheduleModelTypesViewPager2Adapter(val modelTypes: ArrayList<ModelType>, val fragment: Fragment): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = modelTypes.count()

    override fun createFragment(position: Int): Fragment {
        return ScheduleModelsFragment.newInstance(modelTypes[position].name!!)
    }
}