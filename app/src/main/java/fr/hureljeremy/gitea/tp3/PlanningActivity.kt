package fr.hureljeremy.gitea.tp3

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fr.hureljeremy.gitea.tp3.data.AppDatabase
import fr.hureljeremy.gitea.tp3.data.User
import fr.hureljeremy.gitea.tp3.services.Auth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import fr.hureljeremy.gitea.tp3.services.Calendar
import fr.hureljeremy.gitea.tp3.view.PopUpForm
import fr.hureljeremy.gitea.tp3.view.PopUpFormListener

class PlanningActivity : AppCompatActivity() {

    private lateinit var tvSlot1: TextView
    private lateinit var tvSlot2: TextView
    private lateinit var tvSlot3: TextView
    private lateinit var tvSlot4: TextView

    private lateinit var btnSlot1: Button
    private lateinit var btnSlot2: Button
    private lateinit var btnSlot3: Button
    private lateinit var btnSlot4: Button

    private lateinit var btnSavePlanning: Button
    private lateinit var tvDateToday: TextView
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton

    private lateinit var database: AppDatabase
    private var userId: Int = -1
    private  lateinit var user : User

    private var authService: Auth? = null
    private lateinit var  calendarService: Calendar
    private var boundToCalendarService = false

    private var currentDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
    }


    private val connectionAuth  = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as Auth.LocalBinder
            authService = binder.getService()
            user = authService?.getUser()!!
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            authService = null

        }
    }

    private val connectionCalendar = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as Calendar.LocalBinder
            calendarService = binder.getService()
            boundToCalendarService = true
            lifecycleScope.launch {
                loadCalendar()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            boundToCalendarService = false
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, Auth::class.java), connectionAuth, BIND_AUTO_CREATE)
        bindService(Intent(this, Calendar::class.java), connectionCalendar, BIND_AUTO_CREATE)

        tvSlot1 = findViewById(R.id.tvSlot1)
        tvSlot2 = findViewById(R.id.tvSlot2)
        tvSlot3 = findViewById(R.id.tvSlot3)
        tvSlot4 = findViewById(R.id.tvSlot4)

        btnSlot1 = findViewById(R.id.btnEditSlot1)
        btnSlot2 = findViewById(R.id.btnEditSlot2)
        btnSlot3 = findViewById(R.id.btnEditSlot3)
        btnSlot4 = findViewById(R.id.btnEditSlot4)

        btnSavePlanning = findViewById(R.id.btnSavePlanning)
        tvDateToday = findViewById(R.id.tvDateToday)
        btnNext = findViewById(R.id.btnNextDay)
        btnPrevious = findViewById(R.id.btnPreviousDay)





        currentDate = SimpleDateFormat("dd/M/yyyy").format(Date())

        btnNext.setOnClickListener {
            val sdf = SimpleDateFormat("dd/M/yyyy")

            currentDate = sdf.format(Date(((sdf.parse(currentDate))?.time ?: Date().time) + 86400000))

            lifecycleScope.launch {
                loadCalendar()
            }
        }

        btnPrevious.setOnClickListener {

            val sdf = SimpleDateFormat("dd/M/yyyy")
            currentDate = sdf.format(Date(((sdf.parse(currentDate))?.time ?: Date().time ) - 86400000))

            lifecycleScope.launch {
                loadCalendar()
            }

        }

        btnSavePlanning.setOnClickListener {
            lifecycleScope.launch {
                calendarService.updateEvent(user, currentDate, tvSlot1.text.toString(), tvSlot2.text.toString(), tvSlot3.text.toString(), tvSlot4.text.toString())

            }
        }

        btnSlot1.setOnClickListener {
            editSlot(1)
        }

        btnSlot2.setOnClickListener {
            editSlot(2)
        }

        btnSlot3.setOnClickListener {
            editSlot(3)
        }

        btnSlot4.setOnClickListener {
            editSlot(4)
        }

        lifecycleScope.launch {
            loadCalendar()
        }



    }

    override fun onStop() {
        super.onStop()
        unbindService(connectionAuth)
    }


    fun editSlot(slot: Int) {
        val slotText = when (slot) {
            1 -> tvSlot1.text.toString()
            2 -> tvSlot2.text.toString()
            3 -> tvSlot3.text.toString()
            4 -> tvSlot4.text.toString()
            else -> ""
        }
        val dialog = PopUpForm(slot, slotText)
        dialog.setListener(object: PopUpFormListener {
            override fun onTextSaved(slot: Int, text: String) {
                when (slot) {
                    1 -> tvSlot1.text = text
                    2 -> tvSlot2.text = text
                    3 -> tvSlot3.text = text
                    4 -> tvSlot4.text = text
                }
            }
        })
        dialog.show(supportFragmentManager, "EditSlotDialogFragment")
    }



    private suspend fun loadCalendar() {

        tvDateToday.text = currentDate
        while (!boundToCalendarService) {
            return
        }
      val t = calendarService.getEvents(user, currentDate)
       if (t.isSuccess) {
              val events = t.getOrNull()!!
              tvSlot1.setText(events.slot1)
                tvSlot2.setText(events.slot2)
                tvSlot3.setText(events.slot3)
                tvSlot4.setText(events.slot4)
         } else {
              Log.e("PlanningActivity", "Error while loading calendar")
           tvSlot1.setText("")
           tvSlot2.setText("")
           tvSlot3.setText("")
           tvSlot4.setText("")
       }
    }



}