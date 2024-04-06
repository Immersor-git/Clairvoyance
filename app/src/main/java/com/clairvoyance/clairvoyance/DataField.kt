package com.clairvoyance.clairvoyance

import java.util.UUID
data class DataField (
    val dataType: DataType,
    var data: Any?,
    val tag: String,
    var id: UUID = UUID.randomUUID(),
) {

}
