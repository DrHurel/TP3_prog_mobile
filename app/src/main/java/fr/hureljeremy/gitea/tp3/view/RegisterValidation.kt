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
import android.widget.Button
import fr.hureljeremy.gitea.tp3.R
import fr.hureljeremy.gitea.tp3.data.User
import fr.hureljeremy.gitea.tp3.services.Auth
import fr.hureljeremy.gitea.tp3.services.NavigationService

private const val ARG_LOGIN = "login"
private const val ARG_LAST_NAME = "last_name"
private const val ARG_FIRST_NAME = "first_name"
private const val ARG_BIRTH_DATE = "birth_date"
private const val ARG_PHONE = "phone"
private const val ARG_EMAIL = "email"
private const val ARG_MUSIC = "music"
private const val ARG_READING = "reading"
private const val ARG_SPORT = "sport"

class RegisterValidation : Fragment() {
    private var login: String? = null
    private var name: String? = null
    private var firstName: String? = null
    private var birthDate: String? = null
    private var phone: String? = null
    private var email: String? = null
    private var music: Boolean = false
    private var reading: Boolean = false
    private var sport: Boolean? = false
    private var submitButton: Button? = null


    private lateinit var navigationService: NavigationService
    private lateinit var authService: Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            login = it.getString(ARG_LOGIN)
            name = it.getString(ARG_LAST_NAME)
            firstName = it.getString(ARG_FIRST_NAME)
            birthDate = it.getString(ARG_BIRTH_DATE)
            phone = it.getString(ARG_PHONE)
            email = it.getString(ARG_EMAIL)
            music = it.getBoolean(ARG_MUSIC)
            reading = it.getBoolean(ARG_READING)
            sport = it.getBoolean(ARG_SPORT)


        }

        submitButton = view?.findViewById(R.id.validation_submit_btn)

        submitButton?.setOnClickListener {
            val result = authService.register(
               User(
                   login = login!!,
                   password = "password",
                   surname = name!!,
                   name = firstName!!,
                   birthdate = birthDate!!,
                   phone = phone!!,
                   email = email!!,
                   music = music!!,
                   reading = reading!!,
                   sport = sport!!,

               )
            )
            if (result.isSuccess) {
                navigationService.navigate(requireContext(), "auth", Bundle().apply {
                    putString("action", "login")
                })}
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscription_validation, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            login: String, name: String, surname: String, birthDate: String,
            phoneNumber: String, email: String,music : String, reading : String, sport : String
        ) =
            RegisterValidation().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOGIN, login)
                    putString(ARG_LAST_NAME, name)
                    putString(ARG_FIRST_NAME, surname)
                    putString(ARG_BIRTH_DATE, birthDate)
                    putString(ARG_PHONE, phoneNumber)
                    putString(ARG_EMAIL, email)
                    putString(ARG_MUSIC, music)
                    putString(ARG_READING, reading)
                    putString(ARG_SPORT, sport)

                }
            }
    }

    private val navigationConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as NavigationService.LocalBinder
             navigationService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    private  val authConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as Auth.LocalBinder
            authService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onStart() {
        super.onStart()
        val navigationIntent = Intent(context, NavigationService::class.java)
        val authIntent = Intent(context, Auth::class.java)
        context?.bindService(navigationIntent, navigationConnection, Context.BIND_AUTO_CREATE)
        context?.bindService(authIntent, authConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        context?.unbindService(navigationConnection)
        context?.unbindService(authConnection)
    }



}