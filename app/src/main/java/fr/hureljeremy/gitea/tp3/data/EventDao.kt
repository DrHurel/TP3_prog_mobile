package fr.hureljeremy.gitea.tp3.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Update
    suspend fun updatePlanning(planning: Event)

    @Query("SELECT * FROM event WHERE login = :login AND date = :date")
    suspend fun getPlanningByUserAndDate(login: String, date: String): Event?

    @Query("SELECT * FROM event WHERE login = :login ORDER BY date DESC")
    suspend fun getAllPlanningsByUser(login: String): List<Event>
}