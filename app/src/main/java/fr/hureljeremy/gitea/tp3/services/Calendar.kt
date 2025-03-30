package fr.hureljeremy.gitea.tp3.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class Calendar : Service() {

    private val events = mutableListOf<String>()
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): Calendar = this@Calendar
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    public fun addEvent(title: String, description: String, date: String): Result<String> {
        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            return Result.failure(Exception("Title, description or date is empty"))
        }

        if (title.length > 50 || description.length > 100) {
            return Result.failure(Exception("Title or description is too long"))
        }

        return Result.success("Event added")
    }

    public fun removeEvent(title: String): Result<String> {
        if (title.isEmpty()) {
            return Result.failure(Exception("Title is empty"))
        }

        return Result.success("Event removed")
    }

    public fun updateEvent(title: String, description: String, date: String): Result<String> {
        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            return Result.failure(Exception("Title, description or date is empty"))
        }

        if (title.length > 50 || description.length > 100) {
            return Result.failure(Exception("Title or description is too long"))
        }

        return Result.success("Event updated")
    }

    public fun getEvents(): Result<List<String>> {
        return Result.success(listOf("Event 1", "Event 2", "Event 3"))
    }


    private fun loadEvents(): List<String> {
        return listOf("Event 1", "Event 2", "Event 3")
    }
}