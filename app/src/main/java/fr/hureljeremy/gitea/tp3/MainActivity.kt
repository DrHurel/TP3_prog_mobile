package fr.hureljeremy.gitea.tp3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import fr.hureljeremy.gitea.tp3.services.Auth
import fr.hureljeremy.gitea.tp3.services.NavigationService
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    data class Destination(val name: Pages, val activity: Class<out AppCompatActivity>)

    lateinit var navigationService: NavigationService
    private var bound = false

    private val pages = listOf(
        Destination(Pages.HOME, MainActivity::class.java),
        Destination(Pages.AUTH, AuthActivity::class.java),
        Destination(Pages.PLANNING, PlanningActivity::class.java)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.getLogger("MainActivity").info("onCreate")
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.home)

        val btnNewRegistration =
            findViewById<
                    com.google.android.material.button.MaterialButton
                    >(R.id.buttonNewRegistration)
               val btnLogin = findViewById<com.google.android.material.button.MaterialButton>(R.id.buttonLogin)

                btnNewRegistration.setOnClickListener {
                    register(this)
                }

                btnLogin.setOnClickListener {
                    login(this)
                }


    }


    private  fun login(
        context: Context
    ) {

        if (authService.isUserLoggedIn()) {
            navigationService.navigate(context, Pages.PLANNING)
            return
        }

        navigationService.navigate(context, Pages.AUTH,
            Bundle().apply {
                putString("action", "login")
            }
        )
    }

    private fun register(
        context: Context
    ) {
        navigationService.navigate(context, Pages.AUTH,
            Bundle().apply {
                putString("action", "register")
            }
        )
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as NavigationService.LocalBinder
            navigationService = binder.getService()
            if (!bound) {
                val registered = navigationService.getDestinations()
                for (page in pages) {
                    if (registered.contains(page.name)) {
                        continue
                    }
                    navigationService.registerDestination(page.name, page.activity)
                }
            }
            bound = true


        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    lateinit var authService: Auth

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
        Intent(this, NavigationService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)

        }
        Intent(this, Auth::class.java).also { intent ->
            bindService(intent, connectionAuth, Context.BIND_AUTO_CREATE)
        }


    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(connection)
            bound = false
        }
    }


}