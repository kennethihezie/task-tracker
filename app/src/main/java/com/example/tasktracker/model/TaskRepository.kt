package com.example.tasktracker.model

import androidx.lifecycle.LiveData
import io.realm.RealmModel
import io.realm.RealmResults

class TaskRepository(private val taskDao: TaskDao) {
    fun getTasksByAscending(): LiveData<RealmResults<Task>> {
        return taskDao.getTasksByAscending()
    }

    fun getTasksByDescending(): LiveData<RealmResults<Task>> {
        return taskDao.getTasksByDescending()
    }

    fun getTasksByTimeStamp(): LiveData<RealmResults<Task>> {
        return taskDao.getTasksByTimeStamp()
    }

    fun getTaskById(id: String): Task?{
        return taskDao.getTaskById(id)
    }

    fun createTask(task: Task){
        taskDao.createTask(task)
    }

    fun updateTask(task: Task){
        taskDao.updateTask(task)
    }


    fun <T : RealmModel> copyFromRealm(realmObject: T): T?{
        return taskDao.copyFromRealm(realmObject)
    }

    fun deleteTask(id: Int){
        taskDao.deleteTask(id)
    }
}