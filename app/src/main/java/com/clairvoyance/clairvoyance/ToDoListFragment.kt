package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale
import java.util.UUID

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
        val appViewModel = mainActivity.getAppViewModel();
        Log.d("ViewModel Test","ToDoList: " + appViewModel.getTestString());

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ToDoListScreen(openFragment = openFragment, appViewModel.taskViewModel);
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ToDoListScreen(
        openFragment: (fragment: Fragment) -> Unit,
        taskViewModel: TaskViewModel
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
        val taskList2 = taskViewModel.getArchivedTasks()
        var selectedTask : Task? by remember { mutableStateOf(null) }
        fun setSelectedTask(task : Task, selected : Boolean) {
            Log.d("Task List","Set selected: "+selected)
            if (task == selectedTask && !selected) selectedTask = null;
            if (task != selectedTask && selected) selectedTask = task;
            return
        }
        LazyColumn {
            items(taskList) { task ->
                TaskCard(task, taskViewModel, onLongClick, updateTaskFocus, openFragment, task == selectedTask,::setSelectedTask)
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
        openFragment: (fragment: Fragment) -> Unit,
        isSelected: Boolean,
        setSelectedTask : (Task, Boolean) -> Unit
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(5.dp)
                .animateContentSize()
                .combinedClickable(
                    onClick = {
                        // View task
                        //mainActivity.displayTask(task)
                        //openFragment(GraphViewFragment(task))
                        setSelectedTask(task, !isSelected);
                    },
                    onLongClick = {
                        updateTaskFocus(task)
                        onLongClick()
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
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                taskViewModel.deleteTaskItem(task)
                            }
                    )
                }
            }
            if (isSelected) {
                Column(
                    //verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(12.dp) // Outer padding
                        .padding(12.dp), // Inner padding)
                ) {
                    Row() {
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = task.desc,
                            fontSize = 16.sp
                        )
                    }
                    Row() {
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = task.startTime.toString(),
                            fontSize = 16.sp
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = task.endTime.toString(),
                            fontSize = 16.sp
                        )
                    }
                    Column {
                        task.dataFields.forEach {
                            DataFieldRow(it)
                        }
                    }
                    Button(
                        onClick = {
                            openFragment(GraphViewFragment(task, openFragment))
                        }
                    ) {
                        Text(text = "View Data Graphs")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DataFieldRow(
        df : DataField
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(5.dp)
        ) {
            if (df.dataType == DataType.TEXT) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = df.data.toString(),
                    fontSize = 16.sp
                )
            }
            else if (df.dataType == DataType.NUMBER) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = df.data.toString(),
                    fontSize = 16.sp
                )
            }
            else if (df.dataType == DataType.DATE) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "Date Error",
                    fontSize = 16.sp
                )
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
        var desc by remember { mutableStateOf("") }
        var startTime by remember { mutableStateOf(taskState.startTime) }
        var endTime by remember { mutableStateOf(taskState.endTime) }
        val dataFieldList = remember { mutableStateListOf<DataField>() }
        var date by remember { mutableStateOf(LocalDate.now()) }

        name = taskState.name
        desc = taskState.desc
//        startTime = taskState.startTime
//        endTime = taskState.endTime
        dataFieldList.addAll(taskState.dataFields)
//        date = taskState.date

        val timePickerState = rememberTimePickerState()
        var showTimePicker by remember { mutableStateOf(false) }
        var isStartTime by remember { mutableStateOf(false) }

        val datePickerState = rememberDatePickerState()
        var showDatePicker by remember { mutableStateOf(false) }
        var isDataFieldDate by remember { mutableStateOf(false) }
        var dataFieldFocusID by remember { mutableStateOf("")}

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
                    .fillMaxHeight()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(15.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    item {
                        // Load task template
                        LoadTaskTemplate(
                            taskViewModel = taskViewModel,
                            dataFieldList = dataFieldList
                        )
                        // Save task template
                        SaveTaskTemplate(
                            dataFieldList = dataFieldList,
                            taskViewModel = taskViewModel
                        )
                        // Name text field
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Task Name") },
                        )
                        // Desc text field
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = desc,
                            onValueChange = { desc = it },
                            label = { Text("Task Description") }

                        )
                        // Start time button
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showTimePicker = true
                                isStartTime = true
                            }
                        ) {
                            Text(text = if (startTime.toString() == "null") "Start Time" else startTime.toString())
                        }
                        // End time button
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showTimePicker = true
                                isStartTime = false
                            }
                        ) {
                            Text(text = if (endTime == null) "End Time" else endTime.toString())
                        }
                        // Date button
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showDatePicker = true
                            }
                        ) {
                            Text(text = if (date == null) "Date" else date.toString())
                        }

                        // TextField to input number of hours
                        var hours by remember { mutableStateOf("") }
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = hours,
                            onValueChange = { hours = it },
                            label = { Text("Number of Hours") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                //Put logic to schedule task here

                            }


                        ) {
                            Text("Schedule Task")
                        }





                    }


                    // Display Data Field List
                    items(dataFieldList) { dataField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                modifier = Modifier.weight(1f),
                                value = dataField.tag,
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                                onValueChange = {
                                    dataFieldList[dataFieldList.indexOf(dataField)] = dataField.copy(tag = it)
                                }
                            )

                            Image(
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = "",
                                modifier = Modifier
                                    .clickable {
                                        dataFieldList.remove(dataField)
                                    }
                            )
                        }

                        when(dataField.dataType) {
                            DataType.TEXT -> {
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
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
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        isDataFieldDate = true
                                        dataFieldFocusID = dataField.id
                                        showDatePicker = true
                                    }
                                ) {
                                    Text(text = (dataField.data as LocalDate).toString() )
                                }
                            }
                            DataType.NUMBER -> {
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
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

                                //I'm not sure how to open the new screens from within here

                            }
                            DataType.AUDIO -> {

                            }
                            DataType.CHECKBOX -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Image(
                                        painter = painterResource(id = (dataField.data as Checkbox).imageResource()),
                                        contentDescription = "",
                                        modifier = Modifier.clickable {
                                            val oldData = dataField.data as Checkbox
                                            val newData = oldData.copy(isCompleted = !oldData.isCompleted)
                                            dataFieldList[dataFieldList.indexOf(dataField)] = dataField.copy(data = newData)
                                        }
                                    )
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = (dataField.data as Checkbox).desc,
                                        onValueChange = {
                                            val newData = (dataField.data as Checkbox).copy(desc = it)
                                            // Trigger recomposition by replacing data field
                                            dataFieldList[dataFieldList.indexOf(dataField)] = dataField.copy(data = newData)
                                        },
                                        label = { Text("Text") }
                                    )
                                }
                            }
                            DataType.EXCEPTION -> {
                                TODO()
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Buttons to add data fields
                            AddDataFieldButtons(
                                dataFieldList = dataFieldList
                            )
                            // Save button
                            FloatingActionButton(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterHorizontally),
                                onClick = {
                                    // Create a new task if task is null
                                    if (task == null) {
                                        val newTask = Task(
                                            name = name,
                                            desc = desc,
                                            startTime = startTime,
                                            endTime = endTime,
                                            date = date,
                                            dataFields = dataFieldList.toMutableList()
                                        )
                                        taskViewModel.addTaskItem(newTask)
                                        // Else update the existing given task
                                    } else {

                                        taskViewModel.updateTaskItem(
                                            task = task,
                                            name = name,
                                            desc = desc,
                                            startTime = startTime,
                                            endTime = endTime,
                                            date = date,
                                            dataFields = dataFieldList.toMutableList()
                                        )
                                    }

                                    // Reset states
                                    name = ""
                                    desc = ""
                                    startTime = null
                                    endTime = null
                                    date = LocalDate.now()
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

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { /*TODO*/ },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showTimePicker = false

                            val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            Log.d("TIME EVENT", isStartTime.toString())
                            if (isStartTime) startTime = time else endTime = time
                            Log.d("TIME EVENT", startTime?.toString() + " " + endTime?.toString())
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showTimePicker = false
                        }
                    ) { Text("Cancel") }
                }
            )
            {
                TimePicker(state = timePickerState)
            }
        }

        if (showDatePicker) {
            androidx.compose.material3.DatePickerDialog(
                onDismissRequest = { isDataFieldDate = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Weird conversion
                            // Go from Calendar instance to formatted date string to LocalDate
                            val selectedDate = Calendar.getInstance().apply {
                                timeInMillis = datePickerState.selectedDateMillis!!
                            }
                            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val myDate : LocalDate? = LocalDate.parse(dateFormatter.format(selectedDate.time))

                            Log.d("DATE STUFF", isDataFieldDate.toString())
                            Log.d("DATE STUFF", myDate.toString())
                            if (isDataFieldDate) {
                                val df = dataFieldList.find {it.id == dataFieldFocusID}
                                dataFieldList[dataFieldList.indexOf(df)] = df!!.copy(data = myDate)
                            } else {
                                date = myDate
                            }
                            showDatePicker = false
                            isDataFieldDate = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            isDataFieldDate = false
                        }
                    ) { Text("Cancel") }
                }
            )
            {
                DatePicker(state = datePickerState)
            }
        }
    }

    @Composable
    fun AddDataFieldButtons(
        dataFieldList: MutableList<DataField>
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "ADD DATAFIELDS..."
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Row (
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .align(Alignment.CenterHorizontally)
            ){
                // Add Text DataField
                FloatingActionButton(
                    onClick = { dataFieldList.add(DataField(DataType.TEXT, "", "TEXT")) }
                ) {
                    Text(text = "TXT")
                }
                // Add Date DataField
                FloatingActionButton(
                    onClick = { dataFieldList.add(DataField(DataType.DATE, LocalDate.now(), "DATE")) }
                ) {
                    Text(text = "DATE")
                }
                // Add Number DataField
                FloatingActionButton(
                    onClick = { dataFieldList.add(DataField(DataType.NUMBER, "", "NUMBER")) }
                ) {
                    Text(text = "NUM")
                }
                // Add Image DataField
                FloatingActionButton(
                    onClick = {
                        openFragment(CameraActivity1(::openFragment))

                    }
                ) {
                    Text(text = "IMG")
                }
                // Add Audio DataField
                FloatingActionButton(
                    onClick = {
                        openFragment(AudioActivity(::openFragment))
                    }
                ) {
                    Text(text = "AUD")
                }
                // Add Checkbox DataField
                FloatingActionButton(
                    onClick = { dataFieldList.add(DataField(DataType.CHECKBOX, Checkbox(false, ""), "SUBTASK")) }
                ) {
                    Text(text = "BOX")
                }
            }
        }
    }

    @Composable
    fun TimePickerDialog(
        title: String = "Select Time",
        onDismissRequest: () -> Unit,
        confirmButton: @Composable (() -> Unit),
        dismissButton: @Composable (() -> Unit)? = null,
        containerColor: Color = MaterialTheme.colorScheme.surface,
        content: @Composable () -> Unit,
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = containerColor
                    ),
                color = containerColor
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = title,
                        style = MaterialTheme.typography.labelMedium
                    )
                    content()
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        dismissButton?.invoke()
                        confirmButton()
                    }
                }
            }
        }
    }

    @Composable
    fun DatePickerDialog() {

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoadTaskTemplate(
        dataFieldList: MutableList<DataField>,
        taskViewModel: TaskViewModel,
    ) {
        val templates = taskViewModel.templates.collectAsStateWithLifecycle()
        val list = templates.value

        if (list.size == 0) {
            val emptyTemplate = TaskTemplate(name = "Empty Task")
            taskViewModel.addTaskTemplate(TaskTemplate(name = "Empty Task"))
            list.add(emptyTemplate)
        }

        var selected by remember {mutableStateOf(list[0])}

        var isExpanded by remember {mutableStateOf(false)}

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = {isExpanded = !isExpanded}
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(),
                        value = selected.name,
                        onValueChange = {

                        },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        list.forEachIndexed { index, text ->
                            DropdownMenuItem(
                                text = { Text(text = text.name) },
                                onClick = {
                                    selected = list[index]
                                    isExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    dataFieldList.clear()
                    dataFieldList.addAll(selected.dataFields)
                }
            ){
                Text(text = "Load Template")
            }
        }
    }

    @Composable
    fun SaveTaskTemplate(
        dataFieldList: MutableList<DataField>,
        taskViewModel: TaskViewModel,
    ) {
        var templateName by remember { mutableStateOf("") }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = templateName,
                onValueChange = { templateName = it },
                label = { Text("Template Name") },
            )
            Button(
                onClick = {
                    val template = TaskTemplate(name = templateName, dataFields = dataFieldList.toMutableList())
                    taskViewModel.addTaskTemplate(template)

                    // Reset name state
                    templateName = ""
                }
            ){
                Text(text = "Save Template")
            }
        }
    }


    fun findAvailableTimeSlot(taskList: List<Task>, duration: Int): Pair<LocalTime, LocalTime>? {
        val sortedTasks = taskList.sortedBy { it.startTime }

        var newTaskStartTime = LocalTime.of(0, 0)

        for (i in 0 until sortedTasks.size - 1) {
            val currentTask = sortedTasks[i]
            val nextTask = sortedTasks[i + 1]

            if (currentTask.startTime == null ||
                currentTask.endTime == null ||
                nextTask.startTime == null ||
                nextTask.endTime == null
            ) {
                continue
            }

            val currentTaskEndTime = currentTask.endTime
            val nextTaskStartTime = nextTask.startTime
            val gapDuration = Duration.between(currentTaskEndTime, nextTaskStartTime)

            if (gapDuration.toMinutes() >= (duration * 60)) {
                newTaskStartTime = currentTaskEndTime
                break
            }
        }

        if (newTaskStartTime != LocalTime.of(0, 0)) {
            val newTaskEndTime = newTaskStartTime.plusHours(duration.toLong())
            return Pair(newTaskStartTime, newTaskEndTime)
        }

        return null
    }



}