package com.example.appcitasmedicas.ui.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcitasmedicas.databinding.ItemServiceCardBinding
import com.example.appcitasmedicas.ui.services.model.ServiceItem

class ServiceAdapter(
    private val items: List<ServiceItem>,
    private val onClick: (ServiceItem) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.VH>() {
    inner class VH(val binding: ItemServiceCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ServiceItem) {
            binding.tvTitle.text = item.title
            binding.tvSubtitle.text = item.subtitle
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemServiceCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size
}