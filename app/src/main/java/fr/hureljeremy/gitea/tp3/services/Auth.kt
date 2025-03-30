package fr.hureljeremy.gitea.tp3.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class Auth : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    public fun authenticate(username: String, password: String): Result<String> {
        if (username.isEmpty() || password.isEmpty()) {
            return Result.failure(Exception("Username or password is empty"))
        }

        if (username.length > 10 || password.length < 3) {
            return Result.failure(Exception("Username or password is too long or too short"))
        }

        if (password.length < 6) {
            return Result.failure(Exception("Password is too short"))
        }

        return Result.success("User authenticated")
    }

    public fun register(username: String, password: String): Result<String> {
        if (username.isEmpty() || password.isEmpty()) {
            return Result.failure(Exception("Username or password is empty"))
        }

        if (username.length > 10 || password.length < 3) {
            return Result.failure(Exception("Username or password is too long or too short"))
        }

        if (password.length < 6) {
            return Result.failure(Exception("Password is too short"))
        }

        return Result.success("User registered")
    }

    public fun logout(): Result<String> {
        return Result.success("User logged out")
    }

    public  fun isUserLoggedIn(): Boolean {
        return true
    }
}