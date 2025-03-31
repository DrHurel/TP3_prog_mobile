package fr.hureljeremy.gitea.tp3.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument.Page
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import fr.hureljeremy.gitea.tp3.Pages


class NavigationService : Service() {

    private val binder = LocalBinder()
    private val destinations = mutableMapOf<Pages, Class<*>>()
    private val pageIntent = mutableMapOf<Pages, Intent>()
    private var currentDestination: Pages? = null

    inner class LocalBinder : Binder() {
        fun getService(): NavigationService = this@NavigationService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }


    fun navigate(context: Context, page: Pages, apply: Bundle? = null) {
        val destination = destinations[page]
        if (destination != null) {
            this.currentDestination = page
            val intent = pageIntent[page] ?: Intent(context, destination).apply {
                // Ensure theme is preserved across navigation
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // Add theme configuration if needed
                putExtra("android.theme", context.applicationInfo.theme)
            }

            if (pageIntent[page] == null) {
                pageIntent[page] = intent
            }

            if (apply != null) {
                intent.putExtras(apply)
            }

            try {
                context.startActivity(intent)
                Log.d("NavigationService", "Navigating to $page")
            } catch (e: Exception) {
                Log.e("NavigationService", "Navigation failed: ${e.message}")
            }
        } else {
            Log.e("NavigationService", "Destination $page not registered")
        }
    }

    fun registerDestination(page: Pages, ui: Class<*>) {
        destinations[page] = ui
        Log.d("NavigationService", "Registered destination: $page -> $ui")
    }

    fun unregisterDestination(page: Pages) {
        destinations.remove(page)
        Log.d("NavigationService", "Unregistered destination: $page")
    }

    fun getCurrentDestination(): Pages? {
        return currentDestination
    }

    fun clearDestinations() {
        destinations.clear()
        Log.d("NavigationService", "Cleared all destinations")
    }

    fun getDestinations(): List<Pages> {
        return destinations.keys.toList()
    }

}
