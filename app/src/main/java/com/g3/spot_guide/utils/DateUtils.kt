package com.g3.spot_guide.utils

import com.g3.spot_guide.models.TodaySpot
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    fun isTodaySpotValid(todaySpot: TodaySpot?): Boolean {
        todaySpot?.let { spot ->
            val spotDate = dateFormat.parse(todaySpot.date)
            val currentDate = Calendar.getInstance().time

            return spotDate?.after(currentDate) ?: false
        }

        return false
    }

    fun getTodayDate(): String {
        val currentDate = Date(Calendar.getInstance().time.time)
        return dateFormat.format(currentDate)
    }
}