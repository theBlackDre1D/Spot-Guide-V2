package com.g3.base.recyclerView

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class BaseRecyclerViewAdapter<VH: BaseViewHolder, ADAPTER_ITEM: BaseAdapterItem> : RecyclerView.Adapter<BaseViewHolder>() {

    protected val adapterItems = mutableListOf<ADAPTER_ITEM>()

    fun injectData(newItems: List<ADAPTER_ITEM>) {
        adapterItems.clear()
        adapterItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return createAdapterViewHolder(parent, viewType)
    }

    override fun getItemCount() = adapterItems.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindAdapterViewHolder(holder as VH, adapterItems[position], position)
    }

    abstract fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): VH
    abstract fun bindAdapterViewHolder(holder: VH, adapterItem: ADAPTER_ITEM, position: Int)
}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
abstract class BaseAdapterItem