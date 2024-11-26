package uts.c14220010.latihanrecycleview_tasklist

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {
    private var isEditMode = false
    private var taskPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val _tvJudul = findViewById<TextView>(R.id.tvJudul)
        val _etNamaTask = findViewById<EditText>(R.id.namaTask)
        val _etTanggalTask = findViewById<EditText>(R.id.tanggalTask)
        val _etDeskripsiTask = findViewById<EditText>(R.id.deskripsiTask)

        val btnAddTask = findViewById<Button>(R.id.btnAddTask)

        //Cek apakah Request Edit Task
        val intent = intent
        isEditMode = intent.getBooleanExtra("isEditMode", false)
        taskPosition = intent.getIntExtra("taskPosition", -1)

        if (isEditMode) {
            taskPosition = intent.getIntExtra("taskPosition", -1)
            val taskName = intent.getStringExtra("taskName")
            val taskDate = intent.getStringExtra("taskDate")
            val taskDescription = intent.getStringExtra("taskDescription")

            // Display data lama di EditText
            _etNamaTask.setText(taskName)
            _etTanggalTask.setText(taskDate)
            _etDeskripsiTask.setText(taskDescription)

            _tvJudul.text = "Edit Task"
            btnAddTask.text = "Edit Task"
        }

        _etTanggalTask.setOnClickListener{
            showDatePicker(_etTanggalTask)
        }

        btnAddTask.setOnClickListener {
            val nama = _etNamaTask.text.toString()
            val tanggal = _etTanggalTask.text.toString()
            val deskripsi = _etDeskripsiTask.text.toString()

            val isSavedTask = intent.getBooleanExtra("isSavedTask", false)

            if (nama.isNotEmpty() && tanggal.isNotEmpty() && deskripsi.isNotEmpty()) {
                // Kirim data kembali ke MainActivity
                if (isEditMode) {
                    if (isSavedTask) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("isSavedTask", true)
                        intent.putExtra("taskPosition", taskPosition)
                        intent.putExtra("taskName", nama)
                        intent.putExtra("taskDate", tanggal)
                        intent.putExtra("taskDescription", deskripsi)
                        startActivity(intent)
                    } else {
                        MainActivity.dataTask[taskPosition].nama = nama
                        MainActivity.dataTask[taskPosition].tanggal = tanggal
                        MainActivity.dataTask[taskPosition].deskripsi = deskripsi
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    var task = task(nama, tanggal, deskripsi, "Not Yet Started")
                    MainActivity.dataTask.add(task)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun showDatePicker(taskDate: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format tanggal sebagai string
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                taskDate.setText(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }
}