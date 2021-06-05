package ru.nukdotcom.mynmc.ui.management.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.nukdotcom.mynmc.R
import ru.nukdotcom.mynmc.helpers.WebViewHelpers.Companion.initializeManagementView


class GroupsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeManagementView(view, getString(R.string.app_url_schema) + getString(R.string.app_domain) + getString(R.string.app_dir) + "groups")
    }

    companion object {

        fun newInstance() = GroupsFragment()
    }
}