package fr.hureljeremy.gitea.tp3.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import fr.hureljeremy.gitea.tp3.Pages
import fr.hureljeremy.gitea.tp3.R
import fr.hureljeremy.gitea.tp3.data.User
import fr.hureljeremy.gitea.tp3.services.Auth
import fr.hureljeremy.gitea.tp3.services.NavigationService

class Register : Fragment() {

    private lateinit var registerButton: MaterialButton
    private lateinit var name: TextInputEditText
    private lateinit var login: TextInputEditText
    private lateinit var surname: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var birthDate: TextInputEditText
    private lateinit var checkBoxMusic: CheckBox
    private lateinit var checkBoxSport: CheckBox
    private lateinit var checkBoxReading: CheckBox
    private lateinit var phone: TextInputEditText
    private lateinit var email: TextInputEditText

    private lateinit var navigationService: NavigationService
    private lateinit var authService: Auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subscription, container, false)

        initializeViews(view)
        setupClickListeners()

        return view
    }

    private fun initializeViews(view: View) {
        registerButton = view.findViewById(R.id.register_submit_btn)
        name = view.findViewById(R.id.register_name_input)
        login = view.findViewById(R.id.register_login_input)
        surname = view.findViewById(R.id.register_surname_input)
        passwordInput = view.findViewById(R.id.register_password_input)
        birthDate = view.findViewById(R.id.register_birthdate_input)
        checkBoxMusic = view.findViewById(R.id.checkBoxMusique)
        checkBoxSport = view.findViewById(R.id.checkBoxSport)
        checkBoxReading = view.findViewById(R.id.checkBoxLecture)
        phone = view.findViewById(R.id.register_phone_number_input)
        email = view.findViewById(R.id.register_email_input)

    }

    private fun setupClickListeners() {
        registerButton.setOnClickListener {
            handleRegistration()
        }


    }

    private fun handleRegistration() {
        val username = login.text.toString()
        val password = passwordInput.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {

            val result = authService.register(
                User(
                    login = username,
                    password = password,
                    surname = surname.text.toString(),
                    name = name.text.toString(),
                    birthdate = birthDate.text.toString(),
                    phone = phone.text.toString(),
                    email = email.text.toString(),
                    music = checkBoxMusic.isChecked,
                    reading = checkBoxReading.isChecked,
                    sport = checkBoxSport.isChecked
                )
            )
            if (result.isSuccess) {
                navigationService.navigate(requireContext(), Pages.AUTH, Bundle().apply {
                    putString("action", "login")
                })
            } else {
                // Show error
                login.error = result.exceptionOrNull()?.message ?: "Registration failed"
            }


        } else {
            // Show error
            if (username.isEmpty()) login.error = "Username is required"
            if (password.isEmpty()) passwordInput.error = "Password is required"
            if (name.text.toString().isEmpty()) name.error = "Name is required"
            if (surname.text.toString().isEmpty()) surname.error = "Surname is required"
            if (birthDate.text.toString().isEmpty()) birthDate.error = "Birthdate is required"
            if (phone.text.toString().isEmpty()) phone.error = "Phone number is required"
            if (email.text.toString().isEmpty()) email.error = "Email is required"


        }
    }

    private val connectionNav = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as NavigationService.LocalBinder
            navigationService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // Do nothing
        }
    }

    private val connectionAuth = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as Auth.LocalBinder
            authService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // Do nothing
        }
    }

    override fun onStart() {
        super.onStart()
        val context = requireContext()
        Intent(context, NavigationService::class.java).also { intent ->
            context.bindService(intent, connectionNav, Context.BIND_AUTO_CREATE)
        }
        Intent(context, Auth::class.java).also { intent ->
            context.bindService(intent, connectionAuth, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unbindService(connectionNav)
        requireContext().unbindService(connectionAuth)
    }


    companion object {
        @JvmStatic
        fun newInstance() = Register()
    }
}