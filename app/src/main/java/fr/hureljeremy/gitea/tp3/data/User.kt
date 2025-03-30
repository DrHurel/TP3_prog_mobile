package fr.hureljeremy.gitea.tp3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false)
    val login: String,
    val password: String,
    val name: String,
    val surname: String,
    val birthdate: String,
    val phone: String,
    val email: String,
    val music: Boolean,
    val reading: Boolean,
    val sport: Boolean
)
