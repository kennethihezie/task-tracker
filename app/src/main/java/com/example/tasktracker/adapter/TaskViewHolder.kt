package com.example.tasktracker.adapter

import android.content.Context
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktracker.databinding.LayoutTaskBinding
import com.example.tasktracker.model.Task
import com.example.tasktracker.utils.CallbackActionType

class TaskViewHolder(private val binding: LayoutTaskBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bind(task: Task, callback:  (task: Task, action: Int) -> Unit){
        binding.content.text = task.title
        binding.content.setOnClickListener {
            binding.root.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            callback.invoke(task, CallbackActionType.UPDATE.ordinal)
        }

        binding.container.backgroundTintList = ContextCompat.getColorStateList(context, task.backGroundColor!!)

        binding.editTask.setOnClickListener {
            binding.root.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            callback.invoke(task, CallbackActionType.UPDATE.ordinal)
        }

        binding.deleteTask.setOnClickListener {
            binding.deleteTask.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            callback.invoke(task, CallbackActionType.DELETE.ordinal)
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
        }

    }
}