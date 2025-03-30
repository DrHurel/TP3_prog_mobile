package fr.hureljeremy.gitea.tp3.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE login = :login")
    suspend fun getUserByLogin(login: String): User?

    @Query("SELECT * FROM users WHERE login = :login AND password = :password")
    suspend fun getUserByLoginAndPassword(login: String, password: String): User?


    @Query("SELECT * FROM users WHERE login = :login")
    suspend fun getUserByUsername(login: String): User?
}