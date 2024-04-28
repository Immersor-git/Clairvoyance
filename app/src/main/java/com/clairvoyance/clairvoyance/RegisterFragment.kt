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

class RegisterFragment : Fragment() {
    lateinit var mainActivity : MainActivity
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
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val registerButton = view.findViewById<Button>(R.id.btn_register)

        val navLogin = view.findViewById<Button>(R.id.btn_navLogin) //Navigates from register page to login page
        navLogin.setOnClickListener {
            mainActivity.setLoginFragment(false)
        }

        registerButton.setOnClickListener {
            val email = view.findViewById<TextInputEditText>(R.id.email).text.toString()
            val password = view.findViewById<TextInputEditText>(R.id.password).text.toString();
            val confirmPassword = view.findViewById<TextInputEditText>(R.id.confirm_password).text.toString();

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    //accountManager.setCredentials(email, password)
                    accountManager.registerAccount(email, password) { successful ->
                        if (successful) {
                            mainActivity.fragmentNavigation(R.id.nav_account)
                        } else {

                        }
                    }
                }
            }
        }

        return view
    }
}