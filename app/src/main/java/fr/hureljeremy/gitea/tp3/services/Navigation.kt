package fr.hureljeremy.gitea.tp3.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.fragment.app.Fragment

class Navigation : Service() {

    private val pages = mutableMapOf<String,Fragment>()

    override fun onBind(intent: Intent): IBinder {
        return  Binder()
    }

    public fun addPage(name: String, fragment: Fragment) {
        pages[name] = fragment
    }

    public fun move(page : String) {
        
    }
}
