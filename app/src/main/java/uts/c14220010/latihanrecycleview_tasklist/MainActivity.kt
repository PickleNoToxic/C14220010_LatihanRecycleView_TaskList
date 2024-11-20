package uts.c14220010.latihanrecycleview_tasklist

import android.content.Intent
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

class MainActivity : AppCompatActivity() {
    private var arTask = ArrayList<task>()

    private lateinit var _tvNoTask : TextView

    private lateinit var _rvTask : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _rvTask = findViewById<RecyclerView>(R.id.rvTask)

        _tvNoTask = findViewById<TextView>(R.id.tvNoTask)

        val btnAddTask = findViewById<Button>(R.id.btnAddTask)
        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        loadData()
        tampilkanData()
    }

    fun loadData(){
        arTask.clear()
        for (i in dataTask.indices){
            arTask.add(dataTask[i])
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
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Konfirmasi Hapus Data")
                    .setMessage("Apakah Anda yakin ingin menghapus task "+dataTask[position].nama+"?")
                    .setPositiveButton("Ya") { dialog, which ->
                        dataTask.removeAt(position)
                        loadData()
                        tampilkanData()
                    }
                    .setNegativeButton("Tidak") { dialog, which ->
                        Toast.makeText(this@MainActivity, "Batal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }

            override fun editData(position: Int) {
                val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
                intent.putExtra("isEditMode", true)
                intent.putExtra("taskPosition", position)
                intent.putExtra("taskName", dataTask[position].nama)
                intent.putExtra("taskDate", dataTask[position].tanggal)
                intent.putExtra("taskDescription", dataTask[position].deskripsi)
                startActivity(intent)
            }

            override fun changeStatus(position: Int) {
                if(dataTask[position].status == "Not Yet Started"){
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Konfirmasi Memulai Task")
                        .setMessage("Apakah Anda yakin ingin memulai task "+dataTask[position].nama+"?")
                        .setPositiveButton("Ya") { dialog, which ->
                            dataTask[position].status = "On-Going"
                            loadData()
                            tampilkanData()
                        }
                        .setNegativeButton("Tidak") { dialog, which ->
                            Toast.makeText(this@MainActivity, "Batal memulai task", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                } else if(dataTask[position].status == "On-Going"){
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Konfirmasi Menyelesaikan Task")
                        .setMessage("Apakah Anda yakin ingin menyelesaikan task "+dataTask[position].nama+"?")
                        .setPositiveButton("Ya") { dialog, which ->
                            dataTask[position].status = "Finished"
                            loadData()
                            tampilkanData()
                        }
                        .setNegativeButton("Tidak") { dialog, which ->
                            Toast.makeText(this@MainActivity, "Batal menyelesaikan task", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                }
            }
        })
    }

    companion object {
        val dataTask : MutableList<task> = mutableListOf()
    }
}