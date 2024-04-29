package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class ToDoListFragment(
    val openFragment: (fragment: Fragment) -> Unit
) : Fragment() {
    private lateinit var mainActivity: MainActivity;
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater { //Applies theme on loading
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = activity as MainActivity
        val contextThemeWrapper: Context =
            ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
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
                ToDoListScreen(openFragment = openFragment)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ToDoListScreen(
        openFragment: (fragment: Fragment) -> Unit,
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
                openFragment = openFragment,
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
                    .padding(vertical = 50.dp)
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
        updateTaskFocus: (task: Task) -> Unit,
        openFragment: (fragment: Fragment) -> Unit
    ) {
        LazyColumn {
            items(taskList) { task ->
                TaskCard(task, taskViewModel, onLongClick, updateTaskFocus, openFragment)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TaskCard(
        task: Task,
        taskViewModel: TaskViewModel,
        onLongClick: () ->  Unit,
        updateTaskFocus: (task: Task) -> Unit,
        openFragment: (fragment: Fragment) -> Unit
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(5.dp)
                .combinedClickable(
                    onClick = {
                        // View task
                        mainActivity.displayTask(task)
                        //openFragment(GraphViewFragment(task))
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
                    val endTime = task.endTime
                    Text(text = String.format("%02d:%02d", endTime!!.hour, endTime.minute))
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
        val taskState = task ?: Task()

        // Init states
        var name by remember { mutableStateOf("") }
        name = taskState.name

        var desc by remember { mutableStateOf("") }
        desc = taskState.desc

        val dataFieldList = remember { mutableStateListOf<DataField>() }
        dataFieldList.addAll(taskState.dataFields)

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

                    Row(){

                        var showTimePicker by remember { mutableStateOf(false) }


                        Button(
                            onClick = {  }
                        ) {
                            Text(text = "Start Time")
                        }

                        Button(
                            onClick = { }
                        ) {
                            Text(text = "End Time")
                        }


                    }

                    // Display Data Field List
                    DataFieldList(
                        dataFieldList = dataFieldList
                    )
                    // Buttons to add data fields
                    AddDataFieldButtons(
                        dataFieldList = dataFieldList
                    )
                    // Save button
                    Button(
                        modifier = Modifier
                            .padding(50.dp)
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            // Create a new task if task is null
                            if (task == null) {
                                val newTask = Task(
                                    name = name,
                                    desc = desc,
                                    startTime = null,
                                    endTime = null,
                                    date = null,
                                    dataFields = dataFieldList.toMutableList()
                                )
                                taskViewModel.addTaskItem(newTask)
                                // Else update the existing given task
                            } else {
                                taskViewModel.updateTaskItem(
                                    task = task,
                                    name = name,
                                    desc = desc,
                                    dataFields = dataFieldList.toMutableList()
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



    }

    @Composable
    fun DataFieldList(
        dataFieldList: MutableList<DataField>
    ) {
        LazyColumn {
            items(dataFieldList) { dataField ->
                when(dataField.dataType) {
                    DataType.TEXT -> {
                        OutlinedTextField(
                            value = dataField.data as String,
                            onValueChange = {
                                // Trigger recomposition by replacing data field
                                dataFieldList[dataFieldList.indexOf(dataField)] = dataField.copy(data = it)
                            },
                            label = { Text("Text") }
                        )
                    }
                    DataType.DATE -> {
                        Button(
                            onClick = { /*TODO*/ }
                        ) {
                            Text(text = (dataField.data as LocalDate).toString() )
                        }
                    }
                    DataType.NUMBER -> {
                        OutlinedTextField(
                            value = dataField.data as String,
                            onValueChange = {
                                // Only allow digits or decimals
                                if (it.isEmpty() || it.matches("[0-9]{1,13}(\\.[0-9]*)?".toRegex())) {
                                    // Trigger recomposition by replacing data field
                                    dataFieldList[dataFieldList.indexOf(dataField)] = dataField.copy(data = it)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            label = { Text("Number") }
                        )
                    }
                    DataType.IMAGE -> {
                        TODO()
                    }
                    DataType.AUDIO -> {
                        TODO()
                    }
                    DataType.EXCEPTION -> {
                        TODO()
                    }
                }
            }
        }
    }

    @Composable
    fun AddDataFieldButtons(
        dataFieldList: MutableList<DataField>
    ) {
        Column {
            Row (
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .align(Alignment.CenterHorizontally)
            ){
                // Add Text DataField
                Button(
                    onClick = { dataFieldList.add(DataField(DataType.TEXT, "", "")) }
                ) {
                    Text(text = "TXT")
                }
                // Add Date DataField
                Button(
                    onClick = { dataFieldList.add(DataField(DataType.DATE, LocalDate.now(), "")) }
                ) {
                    Text(text = "DATE")
                }
                // Add Number DataField
                Button(
                    onClick = { dataFieldList.add(DataField(DataType.NUMBER, "", "")) }
                ) {
                    Text(text = "NUM")
                }
            }
            Row (
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .align(Alignment.CenterHorizontally)
            ){
                // Add Image DataField
                Button(
                    onClick = {
                        /*TODO*/
                    }
                ) {
                    Text(text = "IMG")
                }
                // Add Audio DataField
                Button(
                    onClick = {
                        /*TODO*/
                    }
                ) {
                    Text(text = "AUD")
                }
            }
        }
    }


