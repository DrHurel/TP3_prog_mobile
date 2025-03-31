package fr.hureljeremy.gitea.tp3.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import fr.hureljeremy.gitea.tp3.data.AppDatabase
import fr.hureljeremy.gitea.tp3.data.Event
import fr.hureljeremy.gitea.tp3.data.EventDao
import fr.hureljeremy.gitea.tp3.data.User

class Calendar : Service() {

    private val events = mutableListOf<String>()
    private val binder = LocalBinder()


    inner class LocalBinder : Binder() {
        fun getService(): Calendar = this@Calendar
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    public suspend fun updateEvent(user:User,
                                date: String,
                                slot1 : String,
                                slot2 : String,
                                slot3 : String,
                                slot4 : String): Result<String> {

        val event = Event(login =user.login, date = date, slot1 = slot1, slot2 = slot2, slot3 = slot3, slot4 = slot4)
        val database = AppDatabase.getDatabase(applicationContext)


        database.planningDao().insertEvent(event)

        return Result.success("Event added")
    }

    public fun removeEvent(title: String): Result<String> {
        if (title.isEmpty()) {
            return Result.failure(Exception("Title is empty"))
        }

        return Result.success("Event removed")
    }



    public suspend fun getEvents(
        user: User,
        date : String
    ): Result<Event> {
        val database = AppDatabase.getDatabase(applicationContext)
        val events = database.planningDao().getPlanningByUserAndDate(user.login, date)
            ?: return Result.failure(Exception("No events found"))

        return Result.success(events)
    }



}