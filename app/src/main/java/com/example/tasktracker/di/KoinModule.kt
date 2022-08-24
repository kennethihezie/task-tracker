package com.example.tasktracker.di

import com.example.tasktracker.model.TaskDao
import com.example.tasktracker.model.TaskRepository
import com.example.tasktracker.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val taskModule = module {
    single { TaskDao() }
    single { TaskRepository(get()) }
    viewModel { TaskViewModel(get()) }
}