package com.g3.spot_guide.utils

import com.g3.spot_guide.models.TodaySpot
import java.text.SimpleDateFormat
import java.util.*

object SpotUtils {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    fun isTodaySpotValid(todaySpot: TodaySpot?): Boolean {
        todaySpot?.let { spot ->
            val spotDate = dateFormat.parse(todaySpot.date)
            val currentDate = Calendar.getInstance().time

            return spotDate?.before(currentDate) ?: false
        }

        return false
    }
}