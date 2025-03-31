package fr.hureljeremy.gitea.tp3.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import fr.hureljeremy.gitea.tp3.Pages
import fr.hureljeremy.gitea.tp3.R
import fr.hureljeremy.gitea.tp3.services.Auth
import fr.hureljeremy.gitea.tp3.services.NavigationService

class Login : Fragment() {

    private lateinit var usernameInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        initializeViews(view)
        setupClickListeners()

        return view
    }

    private fun initializeViews(view: View) {
        usernameInput = view.findViewById(R.id.login_input)
        passwordInput = view.findViewById(R.id.password_input)
        loginButton = view.findViewById(R.id.login_btn)
        registerButton = view.findViewById(R.id.register_btn)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            handleLogin()
        }

        registerButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_container, Register())
                .commit()
        }
    }

    private fun handleLogin() {
        val username = usernameInput.text.toString()
        val password = passwordInput.text.toString()

        val authenticate = authService.authenticate(username, password)
        if (authenticate.isSuccess) {
            // Login successful
            activity?.finish()
            navigationService.navigate(requireContext(), Pages.PLANNING)
        } else {
            // Show error

val errorMessage = authenticate.exceptionOrNull()?.message ?: "Authentication failed"
            usernameInput.error = errorMessage
            passwordInput.error = errorMessage
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Login()
    }

    private lateinit var navigationService: NavigationService
    private lateinit var authService: Auth

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
}