package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class ToDoListFragment : Fragment() {
    private lateinit var mainActivity : MainActivity;
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater { //Applies theme on loading
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = activity as MainActivity
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainContent()
            }
        }
    }


    @Composable
    fun MainContent(
        taskViewModel: TaskViewModel = viewModel()
    ) {
        val taskList = taskViewModel.taskList.collectAsStateWithLifecycle()

        Box{
            // List of tasks
            TaskList(taskList = taskList.value, taskViewModel = taskViewModel)
            Button(
                modifier = Modifier
                    .padding(24.dp)
                    .padding(vertical = 24.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    taskViewModel.updateTaskList(Task(""))
                }
            ) {
                Text(text = "New Task")
            }
        }
    }

    @Composable
    fun TaskList(
        taskList: MutableList<Task>,
        taskViewModel: TaskViewModel
    ) {
        LazyColumn {
            items(taskList) { task ->
                if (task.isExpanded)
                    ExpandedTaskCard(task)
                else
                    TaskCard(task, taskViewModel)
            }
        }
    }

    @Composable
    fun TaskCard(
        task: Task,
        taskViewModel: TaskViewModel
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(12.dp) // Outer padding
                    .padding(12.dp), // Inner padding
            ) {
                Image(
                    painter = painterResource(id = task.imageResource()),
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        taskViewModel.setComplete(task)
                    }
                )

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = task.name,
                    fontSize = 24.sp
                )
            }
        }
    }

    @Composable
    fun ExpandedTaskCard(task: Task) {

    }

    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        Surface(modifier = Modifier.fillMaxSize()) {

        }
        MainContent()
    }
}