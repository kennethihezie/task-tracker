package com.example.tasktracker.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktracker.R
import com.example.tasktracker.adapter.TaskAdapter
import com.example.tasktracker.adapter.TaskProgressAdapter
import com.example.tasktracker.databinding.ActivityMainBinding
import com.example.tasktracker.model.Task
import com.example.tasktracker.utils.Helper
import com.example.tasktracker.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var taskProgressAdapter: TaskProgressAdapter
    private val taskViewModel: TaskViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        adapter = TaskAdapter(this){
            setDataFromRealm(it)
        }

        taskProgressAdapter = TaskProgressAdapter(this)
        binding.taskRv.adapter = adapter
        binding.taskRv.layoutManager = LinearLayoutManager(this)

        binding.taskProgressRv.adapter = taskProgressAdapter

        binding.tvDate.text = Helper.getCurrentDate()
        binding.tvTime.text = Helper.getCurrentTime()

        taskViewModel.getTasks().observe(this, Observer { tasks ->
            adapter.setTasks(tasks)
            taskProgressAdapter.setTasks(tasks)
        })

        ArrayAdapter.createFromResource(this, R.array.alarm_repeat, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRepeat
        }

        binding.spinnerRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        ArrayAdapter.createFromResource(this, R.array.from_to_time, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTimeDurationFrom
        }

        ArrayAdapter.createFromResource(this, R.array.from_to_time, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTimeDurationTo
        }

        ArrayAdapter.createFromResource(this, R.array.counter, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCounter
        }

        binding.fab.setOnClickListener { createOrUpdateTask() }
        binding.updateTask.setOnClickListener { createOrUpdateTask() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOrUpdateTask(){
        if(binding.tvTitle.text.toString().isNotEmpty() && binding.spinnerTimeDurationFrom.selectedItem.toString().isNotEmpty() &&
            binding.spinnerTimeDurationTo.selectedItem.toString().isNotEmpty() &&  binding.spinnerCounter.selectedItem.toString().isNotEmpty()
            && binding.spinnerRepeat.selectedItem.toString().isNotEmpty()){
            val task = Task(
                title = binding.etTask.text.toString(),
                timerFrom = binding.spinnerTimeDurationFrom.selectedItem.toString(),
                timerTo = binding.spinnerTimeDurationTo.selectedItem.toString(),
                counter = binding.spinnerCounter.selectedItem.toString().toInt(),
                repeat = binding.spinnerRepeat.selectedItem.toString(),
                isAllDay = binding.checkBox.isChecked,
                date = Helper.getCurrentDate(),
                time = Helper.getCurrentTime()
            )

            taskViewModel.createOrUpdateTask(task)

            clearOutData()
        } else {
            Toast.makeText(this, "Fill missing data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDataFromRealm(task: Task){
        binding.tvTitle.text = task.title
        binding.tvDate.text = task.date
        binding.tvTime.text = task.time
        binding.checkBox.isChecked = task.isAllDay

        binding.spinnerRepeat.setSelection(this.resources.getStringArray(R.array.alarm_repeat).indexOf(task.repeat), true)
        binding.spinnerTimeDurationFrom.setSelection(this.resources.getStringArray(R.array.from_to_time).indexOf(task.timerFrom), true)
        binding.spinnerTimeDurationTo.setSelection(this.resources.getStringArray(R.array.from_to_time).indexOf(task.timerFrom), true)
        binding.spinnerCounter.setSelection(this.resources.getIntArray(R.array.counter).indexOf(task.counter!!), true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clearOutData(){
        binding.tvTitle.text = ""; binding.tvTitle.hint = "Title"
        binding.tvDate.text = Helper.getCurrentDate()
        binding.tvTime.text = Helper.getCurrentTime()
        binding.checkBox.isChecked = false

        binding.spinnerRepeat.setSelection(0,true)
        binding.spinnerTimeDurationFrom.setSelection(0, true)
        binding.spinnerTimeDurationTo.setSelection(0, true)
        binding.spinnerCounter.setSelection(0, true)
    }


}