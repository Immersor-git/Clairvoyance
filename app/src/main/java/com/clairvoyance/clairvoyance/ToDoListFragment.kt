package com.clairvoyance.clairvoyance

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Paint.Align
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainContent(
        taskViewModel: TaskViewModel = viewModel()
    ) {
        val taskList = taskViewModel.taskList.collectAsStateWithLifecycle()

        // Control Task Sheet Bottom Sheet
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        var taskFocus by remember { mutableStateOf<Task?>(null) }

        Box{
            // List of tasks
            TaskList(
                taskList = taskList.value,
                taskViewModel = taskViewModel,
                updateTaskFocus = { taskFocus = it },
                onLongClick = {
                    scope.launch {
                        showBottomSheet = true
                        sheetState.expand()
                    }
                }
            )
            // New Task
            Button(
                modifier = Modifier
                    .padding(24.dp)
                    .padding(vertical = 24.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    scope.launch {
                        showBottomSheet = true
                        taskFocus = null
                        sheetState.expand()
                    }
                }
            ) {
                Text(text = "New Task")
            }
            TaskSheet(
                task = taskFocus,
                taskViewModel = taskViewModel,
                showBottomSheet = showBottomSheet,
                sheetState = sheetState,
                onDismiss = {
                    scope.launch { sheetState.hide() }
                        .invokeOnCompletion { showBottomSheet = false; taskFocus = null }
                }
            )
        }
    }

    @Composable
    fun TaskList(
        taskList: MutableList<Task>,
        taskViewModel: TaskViewModel,
        onLongClick: () -> Unit,
        updateTaskFocus: (task: Task) -> Unit
    ) {
        LazyColumn {
            items(taskList) { task ->
                TaskCard(task, taskViewModel, onLongClick, updateTaskFocus)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TaskCard(
        task: Task,
        taskViewModel: TaskViewModel,
        onLongClick: () ->  Unit,
        updateTaskFocus: (task: Task) -> Unit
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(5.dp)
                .combinedClickable(
                    onClick = {
                        // View task
                    },
                    onLongClick = {
                        updateTaskFocus(task)
                        onLongClick()
                    }
                )
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

                if (task.endTime != null) {
                    Text(text = "")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TaskSheet(
        task: Task?,
        taskViewModel: TaskViewModel,
        showBottomSheet: Boolean,
        sheetState: SheetState,
        onDismiss: () -> Unit
    ) {
        // Check to see if there is a task object in focus, if not create a blank task as a field template
        val taskState = task ?: Task("")

        val dataFieldList = remember { mutableStateListOf<DataField>() }
        dataFieldList.addAll(taskState.dataFields)

        // Init states
        var name by remember { mutableStateOf("") }
        name = taskState.name

        var desc by remember { mutableStateOf("") }
        desc = taskState.desc

        var dropDownExpanded by remember { mutableStateOf(false) }
        val dropDownOptions = resources.getStringArray(R.array.datafields)
        var selectedOptionText by remember { mutableStateOf(dropDownOptions[0]) }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    // Reset states
                    name = ""
                    desc = ""
                    dataFieldList.clear()

                    onDismiss()
                },
                sheetState = sheetState,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.CenterHorizontally)
                ) {
                    // Name text field
                    OutlinedTextField(
                        value = name,
                        onValueChange = {name = it},
                        label = { Text("Name") },
                    )
                    // Desc text field
                    OutlinedTextField(
                        value = desc,
                        onValueChange = {desc = it},
                        label = { Text("Desc") }

                    )
                    // Display Data Field List
                    LazyColumn {
                        items(dataFieldList) { dataField ->
                            when(dataField.dataType) {
                                DataType.TEXT -> Text(dataField.data as String)
                                DataType.DATE -> TODO()
                                DataType.NUMBER -> TODO()
                                DataType.IMAGE -> TODO()
                                DataType.AUDIO -> TODO()
                                DataType.EXCEPTION -> TODO()
                            }
                        }
                    }
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .align(Alignment.CenterHorizontally)
                    ){
                        // Add Text Datafield
                        Button(
                            onClick = {
                                val df = DataField(DataType.TEXT, "go")
                                dataFieldList.add(df)
                            }
                        ) {
                            Text(text = "TXT")
                        }
                        // Add Date Datafield
                        Button(
                            onClick = {
                                /*TODO*/
                            }
                        ) {
                            Text(text = "DATE")
                        }
                        // Add Number Datafield
                        Button(
                            onClick = {
                                /*TODO*/
                            }
                        ) {
                            Text(text = "NUM")
                        }
                        // Add Image Datafield
                        Button(
                            onClick = {
                                /*TODO*/
                            }
                        ) {
                            Text(text = "IMG")
                        }
                        // Add Audio Datafield
                        Button(
                            onClick = {
                                /*TODO*/
                            }
                        ) {
                            Text(text = "AUD")
                        }
                    }
                    // Save button
                    Button(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            if (task == null) {
                                taskViewModel.updateTaskList(
                                    Task(
                                        name
                                    )
                                )
                            } else {
                                taskViewModel.updateTaskItem(
                                    task,
                                    name,
                                    desc,
                                    dataFieldList.toMutableList()
                                )
                            }

                            // Reset states
                            name = ""
                            desc = ""
                            dataFieldList.clear()

                            onDismiss()
                        }
                    ) {
                        Text("Save Task")
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        Surface(modifier = Modifier.fillMaxSize()) {

        }
        MainContent()
    }
}