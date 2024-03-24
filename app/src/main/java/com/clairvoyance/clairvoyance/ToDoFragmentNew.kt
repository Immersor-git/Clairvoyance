//package com.clairvoyance.clairvoyance
//
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.appcompat.view.ContextThemeWrapper
//import androidx.databinding.DataBindingUtil.setContentView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.clairvoyance.clairvoyance.databinding.ActivityMainBinding
//import com.clairvoyance.clairvoyance.databinding.FragmentToDoBinding
//import com.clairvoyance.clairvoyance.databinding.FragmentToDoNewBinding
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//
//class ToDoFragmentNew : Fragment(), TaskClickListener {
//
//    private lateinit var taskViewModel: TaskViewModel
//    lateinit var mainActivity : MainActivity;
//    private lateinit var view : View
//
//    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater { //Applies theme on loading
//        val inflater = super.onGetLayoutInflater(savedInstanceState)
//        mainActivity = activity as MainActivity
//
//
//        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
//        return inflater.cloneInContext(contextThemeWrapper)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_to_do_new, container, false)
//        super.onCreate(savedInstanceState)
//        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
//
//        val button = view.findViewById<FloatingActionButton>(R.id.newTaskButton)
//
//        button.setOnClickListener {
//            Log.d("onCreateView", "onCreateView: \"I have pressed the button\"")
//            TaskSheet(null, null).show(childFragmentManager, "newTaskTag")
//        }
//
//        // Checks for today's date and sets it in the main task list
//        val dateText = view.findViewById<TextView>(R.id.dailyDate)
//        val formatter = DateTimeFormatter.ofPattern("MMMM dd")
//        val todaysDate = LocalDate.now()
//        val formattedDate = todaysDate.format(formatter)
//        dateText.text = formattedDate
//
//        setRecyclerView()
//        return view
//    }
//
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        binding = FragmentToDoNewBinding.inflate(layoutInflater)
////        setContentView(binding.root)
////        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
////
////        binding.newTaskButton.setOnClickListener {
////            TaskSheet(null, null).show(supportFragmentManager, "newTaskTag")
////        }
////
////        // Checks for today's date and sets it in the main task list
////        val formatter = DateTimeFormatter.ofPattern("MMMM dd")
////        val todaysDate = LocalDate.now()
////        val formattedDate = todaysDate.format(formatter)
////        binding.dailyDate.text = formattedDate
////
////        setRecyclerView()
////    }
//
//    private fun setRecyclerView() {
//        val mainActivity = this
//        val taskRecyerView = view.findViewById<RecyclerView>(R.id.taskListRecylerView)
//        taskViewModel.tasks.observe(viewLifecycleOwner) {
//            taskRecyerView.apply {
//                layoutManager = LinearLayoutManager(context)
//                adapter = TaskAdapter(it, mainActivity)
//            }
//        }
//    }
//
//    override fun editTask(task: Task) {
//        TaskSheet(task, null).show(childFragmentManager, "newTaskTag")
//    }
//
//    override fun completeTask(task: Task) {
//        taskViewModel.setCompleted(task)
//    }
//
//    override fun expandTask(task: Task) {
//        taskViewModel.expandTask(task)
//    }
//
//    override fun newSubTask(parent: Task) {
//        TaskSheet(null, parent).show(childFragmentManager, "newTaskTag")
//    }
//}