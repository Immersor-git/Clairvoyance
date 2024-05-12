package com.clairvoyance.clairvoyance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.view.ContextThemeWrapper
import com.clairvoyance.clairvoyance.HelpItems.CalendarInfo
import com.clairvoyance.clairvoyance.HelpItems.ContactUs
import com.clairvoyance.clairvoyance.HelpItems.GraphInfo
import com.clairvoyance.clairvoyance.HelpItems.NotificationInfo
import com.clairvoyance.clairvoyance.HelpItems.TaskArchiveInfo
import com.clairvoyance.clairvoyance.HelpItems.TaskInfo
import com.clairvoyance.clairvoyance.HelpItems.ThemeInfo

class HelpFragment : Fragment() {
    lateinit var mainActivity : MainActivity;
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater { //Loads current theme based on app state
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = getActivity() as MainActivity


        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_help, container, false)

        val Noti = view.findViewById<Button>(R.id.Notification)
        Noti.setOnClickListener {
            val intent = Intent(activity, NotificationInfo::class.java)
            startActivity(intent)
        }

        val Task = view.findViewById<Button>(R.id.Task)
        Task.setOnClickListener {
            val intent = Intent(activity, TaskInfo::class.java)
            startActivity(intent)
        }

        val Archive = view.findViewById<Button>(R.id.Archive)
        Archive.setOnClickListener {
            val intent = Intent(activity, TaskArchiveInfo::class.java)
            startActivity(intent)
        }

        val Calendar = view.findViewById<Button>(R.id.Calendar)
        Calendar.setOnClickListener {
            val intent = Intent(activity, CalendarInfo::class.java)
            startActivity(intent)
        }

        val Graph = view.findViewById<Button>(R.id.Graphs)
        Graph.setOnClickListener {
            val intent = Intent(activity, GraphInfo::class.java)
            startActivity(intent)
        }

        val Theme = view.findViewById<Button>(R.id.Themes)
        Theme.setOnClickListener {
            val intent = Intent(activity, ThemeInfo::class.java)
            startActivity(intent)
        }

        val Contact = view.findViewById<Button>(R.id.Contact)
        Contact.setOnClickListener {
            val intent = Intent(activity, ContactUs::class.java)
            startActivity(intent)
        }

        return view
    }


}