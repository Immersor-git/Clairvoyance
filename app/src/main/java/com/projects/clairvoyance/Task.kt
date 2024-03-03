package com.projects.clairvoyance

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

// Task data class
//angelo i am in your walls
class Task (
    var name: String,
    var desc: String,
    var startTime: LocalTime?,
    var endTime: LocalTime?,
    var dueTime: LocalTime?,
    var dueDate: LocalDate?,
    var completedDate: LocalDate?,
    var id: UUID = UUID.randomUUID()
) {
}