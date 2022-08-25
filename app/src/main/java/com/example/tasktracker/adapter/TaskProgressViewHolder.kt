package com.example.tasktracker.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.tasktracker.databinding.LayoutProgressBinding
import com.example.tasktracker.model.Task

class TaskProgressViewHolder(private val binding: LayoutProgressBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(task: Task){
        binding.progressBar.setProgress(task.counter!!, true)
        binding.progressBar.max = 100
        binding.textView.text = "Task ${task.taskId}"
    }
}