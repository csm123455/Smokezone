package com.smokezone.ui.calendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.smokezone.R
import com.smokezone.databinding.ActivityCalendarBinding
import com.smokezone.startNoSmokeLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    val binding by lazy { ActivityCalendarBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        drawCalendar()

        bindOnCalendarLongClick()

        setSmokePay()

        binding.noSmokeImageView.setOnClickListener {
            startNoSmokeLink()
        }
    }

    // 한 달 담배값과 일 년 담배값을 계산하여 표시
    private fun setSmokePay() {
        val now = LocalDate.now()
        val monthLoadDateList = loadDateList(this).filter {
            now.year == it.year && now.monthValue == it.month
        }

        val monthSmokeCount = monthLoadDateList.size
        val monthSmokePay = monthSmokeCount * 5000

        binding.monthSmoke.text = "${formatNumber(monthSmokePay)}원"

        val yearLoadDateList = loadDateList(this).filter {
            if (now.year == it.year) {
                true
            } else {
                now.monthValue <= it.month
            }
        }

        val yearSmokeCount = yearLoadDateList.size
        val yearSmokePay = yearSmokeCount * 5000

        binding.yearSmoke.text = "${formatNumber(yearSmokePay)}원"

        binding.monthSmoke10Container.isVisible = monthSmokePay >= 100_000
        binding.monthSmoke20Container.isVisible = monthSmokePay >= 200_000

        binding.yearSmoke50Container.isVisible = yearSmokePay >= 500_000
        binding.yearSmoke200Container.isVisible = yearSmokePay >= 2_000_000
        binding.yearSmoke400Container.isVisible = yearSmokePay >= 4_000_000
        binding.yearSmoke1000Container.isVisible = yearSmokePay >= 10_000_000
        binding.yearSmoke2000Container.isVisible = yearSmokePay >= 20_000_000
    }

    private fun bindOnCalendarLongClick() {
        binding.calendarView.setOnDateLongClickListener { widget, date ->
            val newDates = loadDateList(this).map { it.toCalendarDay() }.toMutableList()

            val dialogView = layoutInflater.inflate(R.layout.dialog_input, null)

            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            // 제거 버튼 기능
            dialogView.findViewById<TextView>(R.id.remove)?.setOnClickListener {
                dialogView.findViewById<EditText>(R.id.smokeCount).text.toString().toIntOrNull()
                    ?.let {
                        for (i in 1..it) {
                            newDates.remove(date)
                        }
                    }

                saveDateList(this, newDates.map { MyDate(it.year, it.month, it.day) })
                widget.invalidateDecorators()
                drawCalendar()
                dialog.dismiss()
            }

            // 추가 버튼 기능
            dialogView.findViewById<TextView>(R.id.add)?.setOnClickListener {
                lifecycleScope.launch {
                    dialogView.findViewById<EditText>(R.id.smokeCount).text.toString().toIntOrNull()
                        ?.let {
                            withContext(Dispatchers.Default) {
                                for (i in 1..it) {
                                    newDates.add(date)
                                }
                            }
                        }

                    withContext(Dispatchers.Default) {
                        saveDateList(this@CalendarActivity, newDates.map { MyDate(it.year, it.month, it.day) })
                    }
                    widget.invalidateDecorators()
                    drawCalendar()
                    dialog.dismiss()
                }
            }
            dialog.show()
            true
        }
    }

    // 달력에 흡연 기록이 있는 날을 확인하여 표시
    private fun drawCalendar() {
        val dates = loadDateList(this).map { it.toCalendarDay() }.toSet().toList()

        binding.calendarView.removeDecorators()

        binding.calendarView.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return dates.contains(day)
            }

            override fun decorate(view: DayViewFacade) {
                val drawable: Drawable? =
                    ContextCompat.getDrawable(this@CalendarActivity, R.drawable.my_custom_circle)
                view.setSelectionDrawable(drawable!!)
            }
        })

        setSmokePay()
    }

    // 저장되어 있는 흡연 기록 데이터 불러오기
    private fun loadDateList(context: Context): List<MyDate> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val json = sharedPreferences.getString(KEY_DATE, null) ?: return emptyList()

        val gson = Gson()
        val type = object : TypeToken<List<MyDate>>() {}.type
        return gson.fromJson(json, type)
    }

    // 흡연 기록 데이터 저장하기
    private fun saveDateList(context: Context, dateList: List<MyDate>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(dateList)

        editor.putString(KEY_DATE, json)
        editor.apply()
    }

    // 문자열을 숫자 형태로 바꾸기 오류가 날 시 그대로 반환
    private fun formatNumber(number: Int): String {
        return runCatching {
            val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
            formatter.format(number)
        }.getOrDefault(number.toString())
    }

    companion object {
        const val PREFS_NAME = "MyPrefs"
        const val KEY_DATE = "KEY_DATE"
    }
}


