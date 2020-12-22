package com.arraigntech.mobioticstask

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.arraigntech.mobioticstask.databinding.HomeItemLayoutBinding
import com.arraigntech.mobioticstask.ui.homeActivity.HomeItem
import com.arraigntech.mobioticstask.utils.Constants.Companion.LIST_STR
import com.arraigntech.mobioticstask.utils.Constants.Companion.USER
import com.google.gson.Gson

class HomeItemAdapter(var list: List<HomeItem>) : RecyclerView.Adapter<HomeItemAdapter.Holder>() {
    class Holder(val binding: HomeItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lists: List<HomeItem>) {
            binding.list = lists.get(adapterPosition)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailPageActivity::class.java)
                var str = Gson().toJson(lists)
                intent.putExtra(LIST_STR, str)
                intent.putExtra(USER,lists.get(adapterPosition).id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeItemLayoutBinding.inflate(inflater)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list)
    }

}