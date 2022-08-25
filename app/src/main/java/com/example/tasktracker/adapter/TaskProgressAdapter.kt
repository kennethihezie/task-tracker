package com.example.tasktracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktracker.databinding.LayoutProgressBinding
import com.example.tasktracker.model.Task

class TaskProgressAdapter(private val context: Context) : RecyclerView.Adapter<TaskProgressViewHolder>(){
    private var tasks = emptyList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskProgressViewHolder {
        LayoutProgressBinding.inflate(LayoutInflater.from(context), parent, false).apply {
            return TaskProgressViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: TaskProgressViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    internal fun setTasks(list: List<Task>){
        this.tasks = list
        notifyDataSetChanged()
    }
}