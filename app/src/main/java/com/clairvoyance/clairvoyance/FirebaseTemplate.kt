package com.clairvoyance.clairvoyance

import java.util.UUID

class FirebaseTemplate(
    var name: String = "",
    var dataFields: List<HashMap<String,String>> = ArrayList(),
    var id: String = UUID.randomUUID().toString()
) {
    constructor() : this("") {}
    companion object {
        fun asTemplate(fbTemplate : FirebaseTemplate) : TaskTemplate {
            return TaskTemplate(
                fbTemplate.name,
                FirebaseTask.loadDataFields(fbTemplate.dataFields),
                fbTemplate.id
            )
        }
    }

    constructor(taskTemplate: TaskTemplate) : this(
       taskTemplate.name,
        FirebaseTask.storeDataFields(taskTemplate.dataFields),
        taskTemplate.id
    ) {}

}