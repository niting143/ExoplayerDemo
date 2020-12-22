package com.arraigntech.mobioticstask

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arraigntech.mobioticstask.databinding.HomeItemLayoutBinding
import com.arraigntech.mobioticstask.ui.homeActivity.HomeItem
import com.arraigntech.mobioticstask.utils.Constants
import com.google.gson.Gson

class DetailItemAdapter(var list: List<HomeItem>, var ids: String) :
    RecyclerView.Adapter<DetailItemAdapter.Holder>() {
    class Holder(val binding: HomeItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lists: List<HomeItem>, ids: String) {
            binding.list = lists.get(adapterPosition)
            if (ids == lists.get(adapterPosition).id) {
                itemView.visibility = View.GONE
                itemView.setLayoutParams(RecyclerView.LayoutParams(0, 0));
            } else {
                itemView.visibility = View.VISIBLE
                itemView.setLayoutParams(
                    RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                );
            }

            itemView.setOnClickListener {
                (itemView.context as Activity).finish()
                val intent = Intent(itemView.context, DetailPageActivity::class.java)
                var str = Gson().toJson(lists)
                intent.putExtra(Constants.LIST_STR, str)
                intent.putExtra(Constants.USER, lists.get(adapterPosition).id)
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
        holder.bind(list, ids)
    }

}