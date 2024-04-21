package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {
    lateinit var mainActivity : MainActivity;
    lateinit var accountManager: AccountManager
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val loginButton = view.findViewById<Button>(R.id.btn_login)

        val navLogin = view.findViewById<Button>(R.id.btn_navRegister) //Navigates from login page to register page
        navLogin.setOnClickListener {
            mainActivity.setLoginFragment(true)
        }

        loginButton.setOnClickListener {
            val email = view.findViewById<TextInputEditText>(R.id.email).text.toString()
            val password = view.findViewById<TextInputEditText>(R.id.password).text.toString();

            if (email.isNotEmpty() && password.isNotEmpty()) {
                accountManager.setCredentials(email, password)
                accountManager.loginAccount { successful ->
                    if (successful) {
                        mainActivity.fragmentNavigation(R.id.nav_account)
                    } else {

                    }
                }
            }
        }

        return view
    }
}