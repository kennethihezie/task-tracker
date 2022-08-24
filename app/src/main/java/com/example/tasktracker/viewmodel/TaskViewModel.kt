package com.example.tasktracker.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tasktracker.model.Task
import com.example.tasktracker.model.TaskRepository
import io.realm.RealmModel
import io.realm.RealmResults

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    fun getTasks(): LiveData<RealmResults<Task>> {
        return taskRepository.getTasks()
    }

    fun getTaskById(id: String): Task?{
        return taskRepository.getTaskById(id)
    }

    fun createOrUpdateTask(task: Task){
        taskRepository.createOrUpdateTask(task)
    }

    fun <T : RealmModel> copyFromRealm(realmObject: T): T?{
        return taskRepository.copyFromRealm(realmObject)
    }

    fun deleteTask(id: String){
        taskRepository.deleteTask(id)
    }
}