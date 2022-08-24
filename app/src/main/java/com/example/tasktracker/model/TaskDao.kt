package com.example.tasktracker.model

import androidx.lifecycle.LiveData
import com.example.tasktracker.utils.DBconstants
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskDao{
    private val realm: Realm by lazy {
        Realm.getDefaultInstance().apply {
            isAutoRefresh = true
        }
    }

    fun createOrUpdateTask(task: Task){
        CoroutineScope(Dispatchers.Main).launch {
            val index = realm.where(Task::class.java).max(DBconstants.TASK_ID)
            var primaryKey = 1
            if(index != null && task.taskId == null){
                primaryKey = index.toInt() + 1
                task.taskId = primaryKey
            }
        }.invokeOnCompletion {
            realm.executeTransactionAsync {
                it.insertOrUpdate(task)
            }
        }
    }

    fun <T : RealmModel> copyFromRealm(realmObject: T): T? {
        return realm.copyFromRealm(realmObject)
    }


    fun getTasks(): LiveData<RealmResults<Task>> {
        return realm.where(Task::class.java).findAllAsync().asLiveData()
    }

    fun getTaskById(id: String): Task? {
        return realm.where(Task::class.java).equalTo(DBconstants.TASK_ID, id).findFirstAsync()
    }

    fun deleteTask(id: String){
        realm.executeTransactionAsync {
            val task = it.where(Task::class.java).equalTo(DBconstants.TASK_ID, id).findFirstAsync()
            task.deleteFromRealm()
        }
    }
}

fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveDataWrapper<T>(this)
