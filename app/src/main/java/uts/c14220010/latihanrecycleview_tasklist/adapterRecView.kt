package uts.c14220010.latihanrecycleview_tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterRecView(private val listTask: ArrayList<task>) : RecyclerView.Adapter<adapterRecView.ListViewHolder>() {

    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun delData(position: Int)

        fun editData(position: Int)

        fun changeStatus(position: Int)

        fun saveTask(position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _namaTask = itemView.findViewById<TextView>(R.id.namaTask)
        var _tanggalTask = itemView.findViewById<TextView>(R.id.tanggalTask)
        var _deskripsiTask = itemView.findViewById<TextView>(R.id.deskripsiTask)
        var _statusTask = itemView.findViewById<TextView>(R.id.statusTask)

        var _btnHapus = itemView.findViewById<Button>(R.id.btnHapus)
        var _btnEdit = itemView.findViewById<Button>(R.id.btnEdit)
        var _btnChangeStatus = itemView.findViewById<Button>(R.id.btnChangeStatus)
        var _btnSaveTask = itemView.findViewById<Button>(R.id.btnSaveTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var task = listTask[position]
        holder._namaTask.setText(task.nama)
        holder._tanggalTask.setText(task.tanggal)
        holder._deskripsiTask.setText(task.deskripsi)
        holder._statusTask.setText(task.status)

        holder._btnHapus.setOnClickListener {
            onItemClickCallback.delData(position)
        }

        holder._btnChangeStatus.setOnClickListener {
            onItemClickCallback.changeStatus(position)
        }

        holder._btnEdit.setOnClickListener {
            onItemClickCallback.editData(position)
        }

        holder._btnSaveTask.setOnClickListener {
            onItemClickCallback.saveTask(position)
        }

        if(task.status != "Not Yet Started"){
            holder._btnEdit.visibility = View.INVISIBLE
        } else {
            holder._btnEdit.visibility = View.VISIBLE
        }

        if(task.status == "Not Yet Started"){
            holder._btnChangeStatus.text = "Start"
        } else if (task.status == "On-Going"){
            holder._btnChangeStatus.text = "Finish"
        }

        if(task.status == "Finished"){
            holder._btnChangeStatus.visibility = View.INVISIBLE
        } else {
            holder._btnChangeStatus.visibility = View.VISIBLE
        }

        if(task.isSaved){
            holder._btnSaveTask.text = "Unsave"
        }
    }
}