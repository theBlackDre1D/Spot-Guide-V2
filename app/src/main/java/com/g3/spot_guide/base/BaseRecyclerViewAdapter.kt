package com.g3.spot_guide.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class BaseRecyclerViewAdapter<VH: BaseViewHolder> : RecyclerView.Adapter<BaseViewHolder>() {

    private val adapterItems = mutableListOf<BaseAdapterItem>()

    fun injectData(newItems: List<BaseAdapterItem>) {
        adapterItems.clear()
        adapterItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return createAdapterViewHolder(parent, viewType)
    }

    override fun getItemCount() = adapterItems.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindAdapterViewHolder(holder as VH, position)
    }

    abstract fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): VH
    abstract fun bindAdapterViewHolder(holder: VH, position: Int)
}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
abstract class BaseAdapterItem