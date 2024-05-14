package com.clairvoyance.clairvoyance

import java.time.LocalDate
import java.util.UUID
data class DataField (
    val dataType: DataType,
    var data: Any?,
    var tag: String,
    var id: String = UUID.randomUUID().toString(),
) {
    constructor() : this(DataType.EXCEPTION,null,"","")

    companion object {
        fun fromHashMap(hs : HashMap<String, String>) : DataField {
            var nType = DataType.valueOf(hs["dataType"]!!)
            var ntag = hs["tag"].orEmpty()
            var nId = hs["id"].orEmpty()
            var rawdata = hs["data"].orEmpty()
            var ndata : Any?;
            ndata = when (nType) {
                DataType.DATE -> DateTimeConverter.stringToDate(rawdata);
                DataType.NUMBER -> rawdata
                DataType.TEXT -> rawdata
                DataType.CHECKBOX -> Checkbox(rawdata.toBoolean(),hs["desc"]!!)
                else -> null
            }
            return DataField(nType, ndata, ntag, nId)
        }
    }

    fun toHashMap() : HashMap<String, String> {
        val hs = HashMap<String, String>();
        var convertedData = "";
        hs.put("dataType", dataType.toString())
        if (dataType == DataType.DATE) {
            convertedData = DateTimeConverter.dateToString(data as LocalDate)
        } else if (dataType == DataType.CHECKBOX) {
            convertedData = (data as Checkbox).isCompleted.toString()
            hs.put("desc", (data as Checkbox).desc)
        }else {
            convertedData = data.toString()
        }
        hs.put("data",convertedData)
        hs.put("tag",tag)
        hs.put("id",id)
        return hs
    }
}
