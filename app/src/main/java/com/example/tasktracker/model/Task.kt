package com.example.tasktracker.model

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Task(
    @PrimaryKey
    var taskId: Int? = null,

    var title: String? = null,

    var timerFrom: String? = null,

    var timerTo: String? = null,

    var counter: Int? = null,

    var repeat: String? = null,

    var isAllDay: Boolean = false,

    var date: String? = null,

    var time: String? = null,

    var status: String? = null,

    var backGroundColor: String? = null
) : RealmObject()