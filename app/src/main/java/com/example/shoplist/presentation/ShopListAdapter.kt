package com.example.shoplist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onItemShopLongClickListener: ((ShopItem) -> Unit)? = null
    var onItemShopClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            ENABLED -> R.layout.item_shop_enabled
            DISABLED -> R.layout.item_shop_disabled
            else -> throw IllegalStateException("Not found viewType = $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnLongClickListener {
            onItemShopLongClickListener?.invoke(getItem(position))
            true
        }
        holder.itemView.setOnClickListener {
            onItemShopClickListener?.invoke(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) {
            ENABLED
        } else {
            DISABLED
        }
    }

    companion object {
        const val ENABLED = 1
        const val DISABLED = 0
    }


    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val itemName = view.findViewById<TextView>(R.id.textItemName)
        private val itemCount = view.findViewById<TextView>(R.id.textItemCount)

        fun bind(item: ShopItem) {
            itemName.text = item.name
            itemCount.text = item.count.toString()
        }
    }
}
