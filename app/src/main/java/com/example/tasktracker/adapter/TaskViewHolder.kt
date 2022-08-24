package com.example.tasktracker.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.tasktracker.databinding.LayoutTaskBinding
import com.example.tasktracker.model.Task

class TaskViewHolder(private val binding: LayoutTaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(task: Task, callback:  (task: Task) -> Unit){
        binding.content.text = task.title
        callback.invoke(task)
    }
}