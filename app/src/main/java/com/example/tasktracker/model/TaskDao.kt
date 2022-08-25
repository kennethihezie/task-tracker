package com.example.tasktracker.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.tasktracker.utils.DBconstants
import io.realm.*
import io.realm.kotlin.deleteFromRealm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskDao{
    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun createTask(task: Task){
        CoroutineScope(Dispatchers.Main).launch {
            val index = realm.where(Task::class.java).max(DBconstants.TASK_ID)
            var primaryKey = 1
            if(index != null){
                primaryKey = index.toInt() + 1
            }
            task.taskId = primaryKey
        }.invokeOnCompletion {
            realm.executeTransactionAsync {
                it.insertOrUpdate(task)
            }
        }
    }

    fun updateTask(task: Task){
        realm.executeTransactionAsync {
            it.insertOrUpdate(task)
        }
    }

    fun <T : RealmModel> copyFromRealm(realmObject: T): T? {
        return realm.copyFromRealm(realmObject)
    }


    fun getTasksByAscending(): LiveData<RealmResults<Task>>{
        return realm.where(Task::class.java).sort(DBconstants.TASK_ID, Sort.ASCENDING).findAllAsync().asLiveData()
    }

    fun getTasksByDescending(): LiveData<RealmResults<Task>>{
        return realm.where(Task::class.java).sort(DBconstants.TASK_ID, Sort.DESCENDING).findAllAsync().asLiveData()
    }

    fun getTasksByTimeStamp(): LiveData<RealmResults<Task>>{
        return realm.where(Task::class.java).sort("timestamp", Sort.DESCENDING).findAllAsync().asLiveData()
    }

    fun getTaskById(id: String): Task? {
        return realm.where(Task::class.java).equalTo(DBconstants.TASK_ID, id).findFirstAsync()
    }

    fun deleteTask(id: Int){
            realm.executeTransactionAsync {
                val task = it.where(Task::class.java).equalTo(DBconstants.TASK_ID, id).findFirst()
                task?.deleteFromRealm()
        }
    }
}

fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveDataWrapper<T>(this)
