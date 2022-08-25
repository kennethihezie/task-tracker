package com.example.tasktracker.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tasktracker.model.Task
import com.example.tasktracker.model.TaskRepository
import io.realm.RealmModel
import io.realm.RealmResults

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    fun getTasksByAscending(): LiveData<RealmResults<Task>> {
        return taskRepository.getTasksByAscending()
    }

    fun getTasksByDescending(): LiveData<RealmResults<Task>> {
        return taskRepository.getTasksByDescending()
    }

    fun getTasksByTimeStamp(): LiveData<RealmResults<Task>> {
        return taskRepository.getTasksByTimeStamp()
    }

    fun getTaskById(id: String): Task?{
        return taskRepository.getTaskById(id)
    }

    fun createTask(task: Task){
        taskRepository.createTask(task)
    }

    fun updateTask(task: Task){
        taskRepository.updateTask(task)
    }

    fun <T : RealmModel> copyFromRealm(realmObject: T): T?{
        return taskRepository.copyFromRealm(realmObject)
    }

    fun deleteTask(id: Int){
        taskRepository.deleteTask(id)
    }
}