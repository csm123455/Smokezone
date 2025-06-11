package com.smokezone.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smokezone.databinding.ItemNotiBinding

class NotiAdapter(
    private val onClickDelete: (NotificationData) -> Unit,
) : RecyclerView.Adapter<NotiAdapter.NotiViewHolder>() {

    private val data = mutableListOf<NotificationData>()

    fun setData(data: List<NotificationData>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        return NotiViewHolder(
            ItemNotiBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        holder.bind(data[position], onClickDelete)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class NotiViewHolder(
        private val binding: ItemNotiBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            notificationData: NotificationData,
            onClickDelete: (NotificationData) -> Unit,
        ) {
            binding.time.text = "${notificationData.hour}:${notificationData.minute}"

            binding.deleteImageView.setOnClickListener {
                onClickDelete(notificationData)
            }
        }
    }
}