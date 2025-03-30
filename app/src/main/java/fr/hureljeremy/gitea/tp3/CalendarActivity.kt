package fr.hureljeremy.gitea.tp3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import fr.hureljeremy.gitea.tp3.data.AppDatabase
import fr.hureljeremy.gitea.tp3.data.Event
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var etSlot1: TextInputEditText
    private lateinit var etSlot2: TextInputEditText
    private lateinit var etSlot3: TextInputEditText
    private lateinit var etSlot4: TextInputEditText
    private lateinit var btnSavePlanning: Button
    private lateinit var tvDateToday: TextView

    private lateinit var database: AppDatabase
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // Récupérer l'ID de l'utilisateur connecté
        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Toast.makeText(this, "Erreur: Utilisateur non identifié", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialiser la base de données Room
        database = AppDatabase.getDatabase(this)

        // Initialiser les vues
        etSlot1 = findViewById(R.id.etSlot1)
        etSlot2 = findViewById(R.id.etSlot2)
        etSlot3 = findViewById(R.id.etSlot3)
        etSlot4 = findViewById(R.id.etSlot4)
        btnSavePlanning = findViewById(R.id.btnSavePlanning)
        tvDateToday = findViewById(R.id.tvDateToday)

        // Afficher la date du jour
        val dateFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE)
        val currentDate = dateFormat.format(Calendar.getInstance().time)
        tvDateToday.text = currentDate

        // Charger le planning existant pour aujourd'hui s'il existe
        loadExistingPlanning()

        // Configurer le bouton d'enregistrement
        btnSavePlanning.setOnClickListener {
            savePlanning()
        }
    }

    private fun loadExistingPlanning() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Calendar.getInstance().time)

        lifecycleScope.launch {
            try {
                val planning = database.planningDao().getPlanningByUserAndDate(userId, today)
                planning?.let {
                    etSlot1.setText(it.slot1 ?: "")
                    etSlot2.setText(it.slot2 ?: "")
                    etSlot3.setText(it.slot3 ?: "")
                    etSlot4.setText(it.slot4 ?: "")
                }
            } catch (e: Exception) {
                Toast.makeText(this@CalendarActivity, "Erreur lors du chargement du planning: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePlanning() {
        val slot1 = etSlot1.text.toString().trim()
        val slot2 = etSlot2.text.toString().trim()
        val slot3 = etSlot3.text.toString().trim()
        val slot4 = etSlot4.text.toString().trim()

        // Vérifier si au moins un créneau est rempli
        if (slot1.isEmpty() && slot2.isEmpty() && slot3.isEmpty() && slot4.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir au moins un créneau horaire", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Calendar.getInstance().time)

        // Créer ou mettre à jour le planning
        val planning = Event(
            userId = userId.toString(),
            date = today,
            slot1 = slot1.ifEmpty { null },
            slot2 = slot2.ifEmpty { null },
            slot3 = slot3.ifEmpty { null },
            slot4 = slot4.ifEmpty { null }
        )

        lifecycleScope.launch {
            try {
                // Vérifier si un planning existe déjà pour aujourd'hui
                val existingPlanning = database.planningDao().getPlanningByUserAndDate(userId, today)

                if (existingPlanning != null) {
                    // Mettre à jour avec l'ID existant
                    val updatedPlanning = planning.copy(id = existingPlanning.id)
                    database.planningDao().updatePlanning(updatedPlanning)
                } else {
                    // Insérer un nouveau planning
                    database.planningDao().insertEvent(planning)
                }

                // Passer à l'activité de synthèse
                val intent = Intent(this@CalendarActivity, PlanningSummary::class.java).apply {
                    putExtra("USER_ID", userId)
                    putExtra("SLOT_1", slot1)
                    putExtra("SLOT_2", slot2)
                    putExtra("SLOT_3", slot3)
                    putExtra("SLOT_4", slot4)
                }
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    this@CalendarActivity,
                    "Erreur lors de l'enregistrement: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}