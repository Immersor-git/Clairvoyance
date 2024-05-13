package com.clairvoyance.clairvoyance

import java.util.UUID

class FirebaseTask(
    var name: String,
    var desc: String,
    var startTime: String, //LocalTime?,
    var endTime: String, //LocalTime?,
    var date: String,

    // Initialized values
    var completedDate: String,
    var isExpanded: Boolean = false,
    var isCompleted: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
    //var children: MutableList<Task> = mutableListOf(),
    var dataFields : List<HashMap<String, String>> = ArrayList<HashMap<String, String>>(), //MutableList<DataField> = mutableListOf(),
    var tags: List<String> = ArrayList<String>(),
    var templateName : String = "",
    ) {

    constructor() : this("","","","","","")

    companion object {
        fun asTask(fbTask : FirebaseTask) : Task {
            return Task(
                fbTask.name,
                fbTask.desc,
                DateTimeConverter.stringToTime(fbTask.startTime),
                DateTimeConverter.stringToTime(fbTask.endTime),
                DateTimeConverter.stringToDate(fbTask.date),
                DateTimeConverter.stringToDate(fbTask.completedDate),
                fbTask.isExpanded,
                fbTask.isCompleted,
                fbTask.id,
                loadDataFields(fbTask.dataFields),
                fbTask.tags.toMutableList(),
                fbTask.templateName
            )
        }
        fun storeDataFields(dfs : List<DataField>) : List<HashMap<String, String>> {
            val dataFields = ArrayList<HashMap<String, String>>()
            for (s in dfs) {
                dataFields.add(s.toHashMap())
            }
            return dataFields
        }

        fun loadDataFields(stringDataFields : List<HashMap<String, String>>) : MutableList<DataField> {
            val dataFields = ArrayList<DataField>()
            for (s in stringDataFields) {
                dataFields.add(DataField.fromHashMap(s))
            }
            return dataFields
        }
    }

    constructor(task : Task) : this(
        task.name,
        task.desc,
        DateTimeConverter.timeToString(task.startTime),
        DateTimeConverter.timeToString(task.endTime),
        DateTimeConverter.dateToString(task.date),
        DateTimeConverter.dateToString(task.completedDate),
        task.isExpanded,
        task.isCompleted,
        task.id,
        storeDataFields(task.dataFields),
        task.tags.toList(),
        task.templateName
    ) {}
}