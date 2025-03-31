package fr.hureljeremy.gitea.tp3.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import fr.hureljeremy.gitea.tp3.data.AppDatabase
import fr.hureljeremy.gitea.tp3.data.User
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest

class Auth : Service() {

    private val binder = LocalBinder()
    private lateinit var database: AppDatabase
    private var userLoggedIn: Boolean = false
    private  var user: User? = null

    inner class LocalBinder : Binder() {
        fun getService(): Auth = this@Auth
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(applicationContext)
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun authenticate(username: String, password: String): Result<String> {
        if (username.isEmpty() || password.isEmpty()) {
            return Result.failure(Exception("Username or password is empty"))
        }

        if (username.length > 10 || password.length < 3) {
            return Result.failure(Exception("Username or password is too long or too short"))
        }

        if (password.length < 6) {
            return Result.failure(Exception("Password is too short"))
        }

        //Load user from database
        return runBlocking {
            try {
                val hashedPassword = hashPassword(password)
                user = database.userDao().getUserByUsername(username)

                if (user != null && user!!.password == hashedPassword) {
                    userLoggedIn = true

                    Result.success("User authenticated")
                } else {
                    Result.failure(Exception("Invalid username or password"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Authentication failed: ${e.message}"))
            }
        }


    }

    fun register(user: fr.hureljeremy.gitea.tp3.data.User): Result<String> {
        if (user.login.isEmpty() || user.password.isEmpty()) {
            return Result.failure(Exception("Username or password is empty"))
        }

        if (user.login.length > 10 || user.password.length < 3) {
            return Result.failure(Exception("Username or password is too long or too short"))
        }

        if (user.password.length < 6) {
            return Result.failure(Exception("Password is too short"))
        }

        return runBlocking {
            try {
                val hashedPassword = hashPassword(user.password)
                val id = database.userDao().insertUser(user.copy(password = hashedPassword))
                Result.success("User registered with id $id")
            } catch (e: Exception) {
                Result.failure(Exception("Registration failed: ${e.message}"))
            }
        }

    }

    fun logout(): Result<String> {
        userLoggedIn = false
        return Result.success("User logged out")
    }

    fun isUserLoggedIn(): Boolean {
        return userLoggedIn
    }

    fun getUser(): User? {
        return user
    }


}
