package com.clairvoyance.clairvoyance

import java.util.UUID

class TaskTemplate(
    var name: String = "",
    var dataFields: MutableList<DataField> = mutableListOf(),
    var id: String = UUID.randomUUID().toString()
) {
    constructor() : this("")
}