package fr.hureljeremy.gitea.tp3

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fr.hureljeremy.gitea.tp3.view.Login
import fr.hureljeremy.gitea.tp3.view.Register

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.getStringExtra("action") == "login") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_container, Login())
                .commit()

        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_container, Register())
                .commit()


        }


    }

    private fun setUpLogin() {
        val username_input = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.login_input)
        val password_input = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.password_input)
        val btnLogin = findViewById<com.google.android.material.button.MaterialButton>(R.id.login_btn)
       val btnRegister = findViewById<TextView>(R.id.register_btn)



        btnLogin.setOnClickListener {
            val username = username_input.text.toString()
            val password = password_input.text.toString()
            if (username == "admin" && password == "admin") {
                // Navigate to home
                finish()
            } else {
                // Show error
                username_input.error = "Invalid credentials"
                password_input.error = "Invalid credentials"
            }
        }

       btnRegister.setOnClickListener {
            btnLogin.setOnClickListener(null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_container, Register())
                .commit()
            setUpRegister()

        }


    }

    private fun setUpRegister() {

    }


}