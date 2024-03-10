package com.clairvoyance.clairvoyance

import java.util.UUID

data class DataField<T> (
    val dataType: DataType,
    var data: Any?,
    var id: UUID = UUID.randomUUID(),
) {

}
