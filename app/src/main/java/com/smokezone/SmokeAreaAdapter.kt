package com.smokezone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smokezone.data.SmokeArea
import com.smokezone.databinding.ItemSmokeAreaBinding
import kotlin.math.*

class SmokeAreaAdapter : RecyclerView.Adapter<SmokeViewHolder>() {

    private val smokeAreas = mutableListOf<SmokeArea>()

    fun setSmokeAreas(smokeAreas: List<SmokeArea>) {
        this.smokeAreas.clear()
        this.smokeAreas.addAll(smokeAreas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmokeViewHolder {
        val binding = ItemSmokeAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmokeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmokeViewHolder, position: Int) {
        holder.bind(smokeAreas[position])
    }

    override fun getItemCount() = smokeAreas.size
}

class SmokeViewHolder(
    private val binding: ItemSmokeAreaBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(smokeArea: SmokeArea) {
        binding.tvTitle.text = smokeArea.title

        binding.tvDistance.text = smokeArea.currentLocation?.let { currentLocation ->
            smokeArea.targetLocation?.let { targetLocation ->
                val distance = calculateDistance(currentLocation, targetLocation)
                formatDistance(distance)
            }
        }
    }

    // 헤버사인 공식을 사용하여 두 지점 사이의 거리를 계산
    private fun calculateDistance(currentLocation: Location, targetLocation: Location): Double {
        val earthRadiusKm = 6371.0

        val deltaLatitude = Math.toRadians(targetLocation.latitude - currentLocation.latitude)
        val deltaLongitude = Math.toRadians(targetLocation.longitude - currentLocation.longitude)

        val a = sin(deltaLatitude / 2) * sin(deltaLatitude / 2) +
                cos(Math.toRadians(currentLocation.latitude)) * cos(Math.toRadians(targetLocation.latitude)) *
                sin(deltaLongitude / 2) * sin(deltaLongitude / 2)
        val c = 2 * asin(sqrt(a))

        return earthRadiusKm * c
    }

    private fun formatDistance(distance: Double): String {
        val roundedDistance = round(distance * 10) / 10
        return "$roundedDistance km"
    }
}