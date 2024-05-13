package com.clairvoyance.clairvoyance

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    companion object {
        fun dateToString(date : LocalDate?) : String {
            if (date == null) return ""
            return "" + date.monthValue + "-" + date.dayOfMonth + "-" + date.year
        }
        fun stringToDate(date : String) : LocalDate? {
            if (date == "") return null;
            val formatter = DateTimeFormatter.ofPattern("M-d-yyyy")
            return LocalDate.parse(date,formatter)
        }

        fun timeToString(time : LocalTime?) : String {
            if (time == null) return ""
            return "" + time.hour + ":" + time.minute;
        }

        fun stringToTime(time : String) : LocalTime? {
            if (time == "") return null;
            val formatter = DateTimeFormatter.ofPattern("H:m")
            return LocalTime.parse(time,formatter);
        }
    }
    /*
    @Test
    fun convert() {
        var d : LocalTime = LocalTime.of(13,2)
        print("LocalTime: "+d.hour+":"+d.minute+"\n")
        var s : String = timeToString(d)
        print("Converted String: "+s+"\n")
        var d2 = stringToTime(s)
        print("LocalTime 2: "+d2!!.hour+":"+d2.minute+"\n")
        var s2 = timeToString(d2)
        print ("double converted: " + s2+"\n")


        var dt : LocalDate = LocalDate.of(2024,5,12)
        print("LocalDate: "+dt.month+"-"+dt.dayOfMonth+"-"+dt.year+"\n")
        var st : String = dateToString(dt)
        print("Converted String: "+st+"\n")
        var dt2 = stringToDate(st)
        print("LocalDate: "+dt2!!.month+"-"+dt2.dayOfMonth+"-"+dt2.year+"\n")
        var st2 = dateToString(dt2)
        print ("double converted: " + st2+"\n")

    }
     */
}