package fr.hureljeremy.gitea.tp3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val login: String,
    val password: String,
    val name: String,
    val surname: String,
    val birthdate: String,
    val phone: String,
    val email: String,
    val interests: String
)
