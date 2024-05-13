package com.clairvoyance.clairvoyance

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class GraphViewFragment(
    val task: Task,
    val openFragment: (fragment: Fragment) -> Unit
) : Fragment() {
    private lateinit var mainActivity: MainActivity
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
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GraphScreen(task = task, taskViewModel = appViewModel.taskViewModel, openFragment)
            }
        }
    }
}

@Composable
fun GraphScreen(
    task: Task,
    taskViewModel: TaskViewModel,
    openFragment: (fragment: Fragment) -> Unit
) {

    Button(
        modifier = Modifier.wrapContentHeight(),
        onClick = { openFragment(ToDoListFragment(openFragment)) }
    )
    {
        Text(text = "Back")
    }

    // Find all numeric data fields and add their tags to a list
    val tags = mutableListOf<String>()
    task.dataFields.forEach {
        if (it.dataType == DataType.NUMBER) tags.add(it.tag)
    }
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentHeight()
    ) {
        items(tags) { tag ->
            GraphView(tag, taskViewModel)
        }
    }
}

@Composable
fun GraphView(
    tag: String,
    taskViewModel: TaskViewModel
) {
    val taskList = taskViewModel.taskList.collectAsStateWithLifecycle().value
    val filteredTaskList =  mutableListOf<Task>()
    var oldestDate = taskList[0].date

    // Iterate through lists to find the oldest date as a baseline compare value
    // and find all tasks that contain df with same tag
    taskList.forEach { task ->
        if (task.date != null) {
            task.dataFields.forEach {df ->
                if (df.dataType == DataType.NUMBER && df.tag == tag) {
                    if (task.date!! < oldestDate) oldestDate = task.date
                    filteredTaskList.add(task)
                }
            }
        }
    }

    Log.d("GRAPHS", taskList.size.toString())
    Log.d("GRAPHS", filteredTaskList.size.toString())

    //val pointsData: List<Point> =
    //   listOf(Point(0f, 40f), Point(1f, 90f), Point(2f, 0f), Point(3f, 60f), Point(4f, 10f))
    val pointsData = mutableListOf<Point>()

    filteredTaskList.forEach { task ->
        val df = task.dataFields.find {it.tag == tag}
        val dx = ChronoUnit.DAYS.between(oldestDate, task.date)
        Log.d("GRAPH STUFF", dx.toString())
        val point = Point(dx.toFloat(), (df!!.data as String).toFloat())
        Log.d("GRAPH STUFF", point.x.toString() + " " + point.y.toString())
        pointsData.add(point)
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Transparent)
        .steps(pointsData.toList().size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(10)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i -> i.toString()
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData.toList(),
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(10.dp),
        lineChartData = lineChartData
    )
}

@Composable
private fun StraightLinechart(pointsData: List<Point>) {
    // Format axis and data
    val xAxisData = AxisData.Builder()
        .axisStepSize(40.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> (1900 + i).toString() }
        .axisLabelAngle(20f)
        .labelAndAxisLinePadding(15.dp)
        .axisLabelColor(Color.Blue)
        .axisLineColor(Color.DarkGray)
        .typeFace(Typeface.DEFAULT_BOLD)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(10)
        .labelData { i -> "${(i * 20)}k" }
        .labelAndAxisLinePadding(30.dp)
        .axisLabelColor(Color.Blue)
        .axisLineColor(Color.DarkGray)
        .typeFace(Typeface.DEFAULT_BOLD)
        .build()

    // Combine axis data into LineChart data
    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(lineType = LineType.Straight(), color = Color.Blue),
                    intersectionPoint = IntersectionPoint(color = Color.Red),
                    selectionHighlightPopUp = SelectionHighlightPopUp(popUpLabel = { x, y ->
                        val xLabel = "x : ${(1900 + x).toInt()} "
                        val yLabel = "y : ${String.format("%.2f", y)}"
                        "$xLabel $yLabel"
                    })
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData

    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = data
    )
}