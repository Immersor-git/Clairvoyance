package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment

class AccountFragment : Fragment() {
    lateinit var mainActivity : MainActivity;
    private lateinit var accountManager: AccountManager
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
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
        accountManager = mainActivity.getAccountManager()
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val signoutButton = view.findViewById<Button>(R.id.btn_SignOut)
        signoutButton.setOnClickListener {
            accountManager.signOut()
            mainActivity.setLoginFragment(false)
        }

        val signedName = view.findViewById<TextView>(R.id.txt_signed_in)
        signedName.setText("Signed in as: "+accountManager.getUserName())
        return view
    }
}