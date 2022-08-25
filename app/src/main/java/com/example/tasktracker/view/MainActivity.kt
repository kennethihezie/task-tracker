package com.example.tasktracker.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktracker.R
import com.example.tasktracker.adapter.TaskAdapter
import com.example.tasktracker.adapter.TaskProgressAdapter
import com.example.tasktracker.databinding.ActivityMainBinding
import com.example.tasktracker.model.Task
import com.example.tasktracker.utils.Helper
import com.example.tasktracker.viewmodel.TaskViewModel
import com.google.android.material.navigation.NavigationBarView
import io.realm.RealmResults
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var taskProgressAdapter: TaskProgressAdapter
    private val taskViewModel: TaskViewModel by viewModel()
    private lateinit var colors: Array<Int>
    private var selectedColor: Int? = null
    private var updateTask: Task? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        colors = arrayOf(R.color.light_green, R.color.light_red, R.color.color_one, R.color.color_two, R.color.color_three, R.color.color_four, R.color.color_five,
        R.color.color_six, R.color.color_ten, R.color.color_eleven, R.color.color_12, R.color.color_14, R.color.color_15, R.color.color_17, R.color.color_18)

        val randomColorIndex = colors.indexOf(colors.random())
        binding.etTask.backgroundTintList = ContextCompat.getColorStateList(this, colors[randomColorIndex])
        selectedColor = colors[randomColorIndex]

        adapter = TaskAdapter(this){task, action ->
            when(action){
                0 -> {
                    deleteTask(task.taskId!!)
                }
                1 -> {
                    setDataFromRealm(task)
                    updateTask = task
                }
            }
        }

        taskProgressAdapter = TaskProgressAdapter(this)
        binding.taskRv.adapter = adapter
        binding.taskRv.layoutManager = LinearLayoutManager(this)

        binding.taskProgressRv.adapter = taskProgressAdapter

        binding.tvDate.text = Helper.getCurrentDate()
        binding.tvTime.text = Helper.getCurrentTime()

        taskViewModel.getTasksByDescending().observe(this, Observer { tasks ->
            adapter.setTasks(tasks)
            taskProgressAdapter.setTasks(tasks)
        })

        ArrayAdapter.createFromResource(this, R.array.alarm_repeat, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRepeat
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

        val counter = arrayOf(25, 50, 75, 100)

        binding.spinnerCounter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(counter[p2]){
                    25 -> {
                        binding.spinnerCounter.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_red)
                    }
                    50 -> {
                        binding.spinnerCounter.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_red)
                    }
                    75 -> {
                        binding.spinnerCounter.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_green)
                    }
                    100 -> {
                        binding.spinnerCounter.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_green)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.fab.setOnClickListener {
            createTask()
        }
        binding.updateTask.setOnClickListener {
            binding.updateTask.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            updateTask?.let { it1 -> updateTask(it1) }
        }

        binding.colorPalette.setOnClickListener {
            changeBackground()
        }

        binding.newTask.setOnClickListener {
            clearOutData()
        }

        binding.sort.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.sort)
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.setOnMenuItemClickListener(object : android.widget.PopupMenu.OnMenuItemClickListener,
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                    when(menuItem?.itemId){
                        R.id.asc -> {
                            getTasksByAscending()
                        }
                        R.id.dsc -> {
                            getTasksByDescending()
                        }
                        R.id.date -> {
                            getTasksByTimeStamp()
                        }
                    }
                    return true
                }
            })

            popupMenu.show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTask(updateTask: Task? = null){
            if(TextUtils.isEmpty(binding.etTask.text.toString()) || TextUtils.isEmpty(binding.spinnerTimeDurationFrom.selectedItem.toString()) ||
                TextUtils.isEmpty(binding.spinnerTimeDurationTo.selectedItem.toString()) ||  TextUtils.isEmpty(binding.spinnerCounter.selectedItem.toString())
                || TextUtils.isEmpty(binding.spinnerRepeat.selectedItem.toString())){
                Toast.makeText(this, "Fill missing data", Toast.LENGTH_SHORT).show()
            } else {
                val task = Task(
                    title = binding.etTask.text.toString(),
                    timerFrom = binding.spinnerTimeDurationFrom.selectedItem.toString(),
                    timerTo = binding.spinnerTimeDurationTo.selectedItem.toString(),
                    counter = binding.spinnerCounter.selectedItem.toString().toInt(),
                    repeat = binding.spinnerRepeat.selectedItem.toString(),
                    isAllDay = binding.checkBox.isChecked,
                    date = Helper.getCurrentDate(),
                    time = Helper.getCurrentTime(),
                    backGroundColor = selectedColor,
                    timestamp = System.currentTimeMillis() / 1000
                )

                taskViewModel.createTask(task)
                Toast.makeText(this, "Created or Updated task", Toast.LENGTH_SHORT).show()

                clearOutData()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTask(task: Task){
        if(TextUtils.isEmpty(binding.etTask.text.toString()) || TextUtils.isEmpty(binding.spinnerTimeDurationFrom.selectedItem.toString()) ||
            TextUtils.isEmpty(binding.spinnerTimeDurationTo.selectedItem.toString()) ||  TextUtils.isEmpty(binding.spinnerCounter.selectedItem.toString())
            || TextUtils.isEmpty(binding.spinnerRepeat.selectedItem.toString())){
            Toast.makeText(this, "Fill missing data", Toast.LENGTH_SHORT).show()
        } else {
            val copy = taskViewModel.copyFromRealm(task)
            copy?.title = binding.etTask.text.toString()
            copy?.timerFrom = binding.spinnerTimeDurationFrom.selectedItem.toString()
            copy?.timerTo = binding.spinnerTimeDurationTo.selectedItem.toString()
            copy?.counter = binding.spinnerCounter.selectedItem.toString().toInt()
            copy?.repeat = binding.spinnerRepeat.selectedItem.toString()
            copy?.isAllDay = binding.checkBox.isChecked
            copy?.date = Helper.getCurrentDate()
            copy?.time = Helper.getCurrentTime()
            copy?.backGroundColor = selectedColor
            copy?.timestamp = System.currentTimeMillis() / 1000

            if (copy != null) {
                taskViewModel.updateTask(copy)
            }
            Toast.makeText(this, "Updated task", Toast.LENGTH_SHORT).show()

            clearOutData()
        }
    }

    @SuppressLint("ResourceType")
    private fun setDataFromRealm(task: Task){
        binding.etTask.setText(task.title)
        binding.tvDate.text = task.date
        binding.tvTime.text = task.time
        binding.checkBox.isChecked = task.isAllDay

        binding.etTask.backgroundTintList = ContextCompat.getColorStateList(this, task.backGroundColor!!)
        binding.spinnerRepeat.setSelection(this.resources.getStringArray(R.array.alarm_repeat).indexOf(task.repeat), true)
        binding.spinnerTimeDurationFrom.setSelection(this.resources.getStringArray(R.array.from_to_time).indexOf(task.timerFrom), true)
        binding.spinnerTimeDurationTo.setSelection(this.resources.getStringArray(R.array.from_to_time).indexOf(task.timerFrom), true)
        binding.spinnerCounter.setSelection(this.resources.getIntArray(R.array.counter).indexOf(task.counter!!), true)

        when(task.counter!!){
            25 -> {
                binding.spinnerCounter.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_red)
            }
            50 -> {
                binding.spinnerCounter.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_red)
            }
            75 -> {
                binding.spinnerCounter.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_green)
            }
            100 -> {
                binding.spinnerCounter.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_green)
            }
        }
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun clearOutData(){
        val randomColorIndex = colors.indexOf(colors.random())
        binding.etTask.setText(""); binding.etTask.hint = "Title"
        binding.tvDate.text = Helper.getCurrentDate()
        binding.tvTime.text = Helper.getCurrentTime()
        binding.checkBox.isChecked = false

        binding.etTask.backgroundTintList = ContextCompat.getColorStateList(this, colors[randomColorIndex])
        selectedColor = colors[randomColorIndex]
        binding.spinnerRepeat.setSelection(0,true)
        binding.spinnerTimeDurationFrom.setSelection(0, true)
        binding.spinnerTimeDurationTo.setSelection(0, true)
        binding.spinnerCounter.setSelection(0, true)
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeBackground(){
        val randomColorIndex = colors.indexOf(colors.random())


        binding.etTask.backgroundTintList = ContextCompat.getColorStateList(this, colors[randomColorIndex])
        selectedColor = colors[randomColorIndex]
    }

    private fun deleteTask(id: Int){
        taskViewModel.deleteTask(id)
    }

    fun getTasksByAscending() {
        taskViewModel.getTasksByAscending().observe(this, Observer { tasks ->
            adapter.setTasks(tasks)
            taskProgressAdapter.setTasks(tasks)
        })
    }

    fun getTasksByDescending() {
        taskViewModel.getTasksByDescending().observe(this, Observer { tasks ->
            adapter.setTasks(tasks)
            taskProgressAdapter.setTasks(tasks)
        })
    }

    fun getTasksByTimeStamp() {
        taskViewModel.getTasksByTimeStamp().observe(this, Observer { tasks ->
            adapter.setTasks(tasks)
            taskProgressAdapter.setTasks(tasks)
        })
    }

}