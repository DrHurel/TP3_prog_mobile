package fr.hureljeremy.gitea.tp3.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.hureljeremy.gitea.tp3.data.AppDatabase
import fr.hureljeremy.gitea.tp3.data.User
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }

    fun getUserByLogin(login: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByLogin(login)
            callback(user)
        }
    }
}
