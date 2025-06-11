package com.smokezone.ui.calendar

import com.prolificinteractive.materialcalendarview.CalendarDay

data class MyDate(val year: Int, val month: Int, val day: Int, val count: Int = 1) {
    fun toCalendarDay(): CalendarDay {
        return CalendarDay.from(year, month, day)
    }
}