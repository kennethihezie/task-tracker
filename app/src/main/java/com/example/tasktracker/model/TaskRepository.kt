package com.example.tasktracker.model

import androidx.lifecycle.LiveData
import io.realm.RealmModel
import io.realm.RealmResults

class TaskRepository(private val taskDao: TaskDao) {
    fun getTasks(): LiveData<RealmResults<Task>> {
        return taskDao.getTasks()
    }

    fun getTaskById(id: String): Task?{
        return taskDao.getTaskById(id)
    }

    fun createOrUpdateTask(task: Task){
        taskDao.createOrUpdateTask(task)
    }

    fun <T : RealmModel> copyFromRealm(realmObject: T): T?{
        return taskDao.copyFromRealm(realmObject)
    }

    fun deleteTask(id: String){
        taskDao.deleteTask(id)
    }
}