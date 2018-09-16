package de.appsfactory.lastfm.ui.home.topalbums

import android.arch.paging.PagedListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.data.NetworkState
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.databinding.ItemNetworkStateBinding
import de.appsfactory.lastfm.databinding.ItemTopAlbumBinding
import de.appsfactory.lastfm.ui.common.base.BaseHolder
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener
import de.appsfactory.lastfm.ui.common.viewmodels.ItemNetworkStateViewModel

class TopAlbumsAdapter(val itemClickListener: ListItemClickListener) : PagedListAdapter<Album, BaseHolder>(Album.DIFF_CALLBACK) {

    private var networkState: NetworkState = NetworkState.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder: BaseHolder

        when (viewType) {
            R.layout.item_top_album -> {
                val binding = ItemTopAlbumBinding.inflate(layoutInflater, parent, false)
                viewHolder = TopAlbumItemViewHolder(binding, itemClickListener)
            }
            R.layout.item_network_state -> {
                val binding = ItemNetworkStateBinding.inflate(layoutInflater, parent, false)
                viewHolder = NetworkStateItemViewHolder(binding, itemClickListener)
            }
            else -> throw IllegalArgumentException("unknown view type")
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_top_album -> (holder as TopAlbumItemViewHolder).onBind(position)
            R.layout.item_network_state -> (holder as NetworkStateItemViewHolder).onBind(position)
        }
    }

    private fun hasExtraRow(): Boolean = networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == super.getItemCount()) {
            R.layout.item_network_state
        } else {
            R.layout.item_top_album
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousItemCount = itemCount
        val previousState = networkState
        val previousExtraRow = hasExtraRow()
        networkState = newNetworkState ?: NetworkState.LOADING
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(previousItemCount)
            } else {
                notifyItemInserted(previousItemCount + 1)
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(previousItemCount - 1)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    inner class TopAlbumItemViewHolder(binding: ItemTopAlbumBinding, itemClickListener: ListItemClickListener) : BaseHolder(binding.root) {

        private val binding: ItemTopAlbumBinding

        init {
            itemView.setOnClickListener {
                itemClickListener.onClick(it, adapterPosition)
            }
            this.binding = binding
        }

        override fun onBind(position: Int) {
            val item = this@TopAlbumsAdapter.getItem(position)!!
            val topAlbumsItemViewModel = TopAlbumsItemViewModel(item)
            binding.viewModel = topAlbumsItemViewModel
        }
    }

    inner class NetworkStateItemViewHolder(binding: ItemNetworkStateBinding, clickListener: ListItemClickListener) : BaseHolder(binding.root) {
        private val binding: ItemNetworkStateBinding
        private val clickListener: ListItemClickListener

        init {
            this.binding = binding
            this.clickListener = clickListener
        }

        override fun onBind(position: Int) {
            val networkStateObj = this@TopAlbumsAdapter.networkState
            val itemNetworkStateViewModel = ItemNetworkStateViewModel(networkStateObj)
            binding.viewModel = itemNetworkStateViewModel
            binding.retryButton.setOnClickListener { view -> clickListener.onRetryClick(adapterPosition) }
        }
    }

}