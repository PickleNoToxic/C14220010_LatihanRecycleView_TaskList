package uts.c14220010.latihanrecycleview_tasklist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private var arTask = ArrayList<task>()

    private var savedTask : MutableList<task> = mutableListOf()

    private lateinit var _tvNoTask : TextView

    private lateinit var _rvTask : RecyclerView

    lateinit var sp : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sp = getSharedPreferences("dataTaskSP", MODE_PRIVATE)

        val gson = Gson()
        val isiSP = sp.getString("spTask", null)
        val type = object : TypeToken<MutableList<task>>() {}.type
        if (isiSP != null) {
            savedTask = gson.fromJson(isiSP, type)
        }

        _rvTask = findViewById<RecyclerView>(R.id.rvTask)

        _tvNoTask = findViewById<TextView>(R.id.tvNoTask)

        val btnAddTask = findViewById<Button>(R.id.btnAddTask)
        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        if(intent.getBooleanExtra("isSavedTask", false)){
            savedTask[intent.getIntExtra("taskPosition", -1)].nama = intent.getStringExtra("taskName")
            savedTask[intent.getIntExtra("taskPosition", -1)].tanggal = intent.getStringExtra("taskDate")
            savedTask[intent.getIntExtra("taskPosition", -1)].deskripsi = intent.getStringExtra("taskDescription")
            saveToSharedPreferences()
        }

        loadData()
        tampilkanData()
    }

    fun loadData(){
        arTask.clear()

        for (task in savedTask) {
            task.isSaved = true
            arTask.add(task)
        }

        for (task in dataTask) {
            task.isSaved = false
            arTask.add(task)
        }

        if(arTask.size == 0){
            _tvNoTask.visibility = TextView.VISIBLE
        } else {
            _tvNoTask.visibility = TextView.GONE
        }
    }

    fun tampilkanData(){
        _rvTask.layoutManager = LinearLayoutManager(this)

        val adapterTask = adapterRecView(arTask)
        _rvTask.adapter = adapterTask

        adapterTask.setOnItemClickCallback(object : adapterRecView.OnItemClickCallback {
            override fun delData(position: Int) {
                val selectedTask = arTask[position]
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Konfirmasi Hapus Data")
                    .setMessage("Apakah Anda yakin ingin menghapus task "+selectedTask.nama+"?")
                    .setPositiveButton("Ya") { dialog, which ->
                        if (selectedTask.isSaved) {
                            savedTask.remove(selectedTask)
                            saveToSharedPreferences()
                        } else {
                            dataTask.remove(selectedTask)
                        }
                        loadData()
                        tampilkanData()
                    }
                    .setNegativeButton("Tidak") { dialog, which ->
                        Toast.makeText(this@MainActivity, "Batal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }

            override fun editData(position: Int) {
                val selectedTask = arTask[position]
                val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
                var index: Int
                if (selectedTask.isSaved) {
                    index = savedTask.indexOf(selectedTask)
                } else {
                    index = dataTask.indexOf(selectedTask)
                }
                intent.putExtra("isEditMode", true)
                intent.putExtra("isSavedTask", selectedTask.isSaved)
                intent.putExtra("taskPosition", index)
                intent.putExtra("taskName", selectedTask.nama)
                intent.putExtra("taskDate", selectedTask.tanggal)
                intent.putExtra("taskDescription", selectedTask.deskripsi)
                intent.putExtra("taskStatus", selectedTask.status)
                startActivity(intent)
            }

            override fun changeStatus(position: Int) {
                val selectedTask = arTask[position]
                if(selectedTask.status == "Not Yet Started"){
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Konfirmasi Memulai Task")
                        .setMessage("Apakah Anda yakin ingin memulai task "+selectedTask.nama+"?")
                        .setPositiveButton("Ya") { dialog, which ->
                            if(selectedTask.isSaved){
                                val index = savedTask.indexOf(selectedTask)
                                savedTask[index].status = "On-Going"
                                saveToSharedPreferences()
                            } else {
                                val index = dataTask.indexOf(selectedTask)
                                dataTask[index].status = "On-Going"
                            }
                            loadData()
                            tampilkanData()
                        }
                        .setNegativeButton("Tidak") { dialog, which ->
                            Toast.makeText(this@MainActivity, "Batal memulai task", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                } else if(selectedTask.status == "On-Going"){
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Konfirmasi Menyelesaikan Task")
                        .setMessage("Apakah Anda yakin ingin menyelesaikan task "+selectedTask.nama+"?")
                        .setPositiveButton("Ya") { dialog, which ->
                            if(selectedTask.isSaved){
                                val index = savedTask.indexOf(selectedTask)
                                savedTask[index].status = "Finished"
                                saveToSharedPreferences()
                            } else {
                                val index = dataTask.indexOf(selectedTask)
                                dataTask[index].status = "Finished"
                            }
                            loadData()
                            tampilkanData()
                        }
                        .setNegativeButton("Tidak") { dialog, which ->
                            Toast.makeText(this@MainActivity, "Batal menyelesaikan task", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                }
            }

            override fun saveTask(position: Int) {
                val selectedTask = arTask[position]
                if(!selectedTask.isSaved){
                    selectedTask.isSaved = true

                    savedTask.add(selectedTask)
                    dataTask.remove(selectedTask)
                } else {
                    selectedTask.isSaved = false
                    savedTask.remove(selectedTask)
                    dataTask.add(selectedTask)
                }
                saveToSharedPreferences()
                loadData()
                tampilkanData()
            }
        })
    }

    fun saveToSharedPreferences() {
        val gson = Gson()
        val editor = sp.edit()
        val json = gson.toJson(savedTask)
        editor.putString("spTask", json)
        editor.apply()
    }

    companion object {
        val dataTask : MutableList<task> = mutableListOf()
    }
}