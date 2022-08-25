package com.example.tasktracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktracker.databinding.LayoutTaskBinding
import com.example.tasktracker.model.Task
import com.example.tasktracker.utils.CallbackActionType

class TaskAdapter(private val context: Context, private val callback:  (task: Task, action: Int) -> Unit) : RecyclerView.Adapter<TaskViewHolder>() {
    private var tasks = emptyList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        LayoutTaskBinding.inflate(LayoutInflater.from(context), parent, false).apply {
            return TaskViewHolder(this, context)
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], callback)
    }

    override fun getItemCount(): Int = tasks.size

    internal fun setTasks(list: List<Task>){
        this.tasks = list
        notifyDataSetChanged()
    }
}