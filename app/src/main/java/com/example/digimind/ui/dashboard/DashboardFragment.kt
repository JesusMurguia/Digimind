package com.example.digimind.ui.dashboard

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.digimind.R
import com.example.digimind.Tarea
import com.example.digimind.Task
import com.example.digimind.ui.home.HomeFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var storage:FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val btn_time: Button = root.findViewById(R.id.btn_time)
        storage= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()

        btn_time.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                btn_time.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(root.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true).show()

        }
        val btn_save = root.findViewById(R.id.btn_save) as Button
        val et_titulo = root.findViewById(R.id.et_task) as EditText
        val checkMonday = root.findViewById(R.id.checkMonday) as CheckBox
        val checkTuesday = root.findViewById(R.id.checkTuesday) as CheckBox
        val checkWednesday = root.findViewById(R.id.checkWednesday) as CheckBox
        val checkThursday = root.findViewById(R.id.checkThursday) as CheckBox
        val checkFriday = root.findViewById(R.id.checkFriday) as CheckBox
        val checkSaturday = root.findViewById(R.id.checkSatuday) as CheckBox
        val checkSunday = root.findViewById(R.id.checkSunday) as CheckBox

        var titulo:String=""
        var time :String=""


        fun task(titulo:String, time:String): HashMap<String, Any> {
            var taskHashMap= hashMapOf(
                "actividad" to titulo,
                "tiempo" to time,
                "email_usuario" to "${auth.currentUser.email}",
                "lu" to checkMonday.isChecked,
                "ma" to checkTuesday.isChecked,
                "mi" to checkWednesday.isChecked,
                "ju" to checkThursday.isChecked,
                "vi" to checkFriday.isChecked,
                "sa" to checkSaturday.isChecked,
                "do" to checkSunday.isChecked
            )
            return taskHashMap
        }
        var taskHashMap= hashMapOf(
            "actividad" to titulo,
            "tiempo" to time,
            "email_usuario" to "${auth.currentUser.email}",
            "lu" to checkMonday.isChecked,
            "ma" to checkTuesday.isChecked,
            "mi" to checkWednesday.isChecked,
            "ju" to checkThursday.isChecked,
            "vi" to checkFriday.isChecked,
            "sa" to checkSaturday.isChecked,
            "do" to checkSunday.isChecked
        )


        btn_save.setOnClickListener{
            titulo = et_titulo.text.toString()
            time = btn_time.text.toString()
            storage.collection("actividades")
                .add(task(titulo, time))
                .addOnSuccessListener {
                    Toast.makeText(root.context, "dd task added" +titulo+time, Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(root.context, "Algo sucedió mal, intente de nuevo", Toast.LENGTH_SHORT).show()
                }
        }


        return root
    }


}
