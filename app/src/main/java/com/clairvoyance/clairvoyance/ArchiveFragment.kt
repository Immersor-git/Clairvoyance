package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

class ArchiveFragment : Fragment() {
    lateinit var mainActivity : MainActivity;
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = getActivity() as MainActivity
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val appViewModel = mainActivity.getAppViewModel();
        Log.d("ViewModel Test","ToDoList: " + appViewModel.getTestString());

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TaskArchiveScreen(appViewModel.taskViewModel);
            }
        }
        //return inflater.inflate(R.layout.fragment_archive, container, false)
    }

    @Composable
    fun TaskArchiveScreen(
        taskViewModel: TaskViewModel
    ) {
        val taskList = taskViewModel.taskArchive.collectAsStateWithLifecycle()

        Box{
            // List of tasks
            TaskList(
                taskList = taskList.value,
                taskViewModel = taskViewModel
            )
        }
    }

    @Composable
    fun TaskList(
        taskList: MutableList<Task>,
        taskViewModel: TaskViewModel,
    ) {
        LazyColumn {
            items(taskList) { task ->
                TaskCard(task, taskViewModel)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TaskCard(
        task: Task,
        taskViewModel: TaskViewModel
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(5.dp)
                .animateContentSize()
                .combinedClickable(
                    onClick = {

                    },
                    onLongClick = {

                    }
                )
        ) {
            Row() {
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

                    Spacer(Modifier.weight(1f))

                    if (task.endTime != null) {
                        val endTime = task.Time(task.endTime!!.toString())
                        Text(text = String.format("%02d:%02d", endTime!!.hour, endTime.minute))
                    }

                    Image(
                        painter = painterResource(id = R.drawable.restore),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                taskViewModel.restoreFromArchive(task)
                            }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                taskViewModel.deleteTaskFromArchive(task)
                            }
                    )
                }
            }
        }
    }
}