package de.appsfactory.lastfm.ui.home.myalbumsscreen

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.databinding.ItemMyAlbumBinding
import de.appsfactory.lastfm.ui.common.base.BaseHolder
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener

class MyAlbumsAdapter(val itemClickListener: ListItemClickListener) : RecyclerView.Adapter<BaseHolder>() {

    var items = mutableListOf<Album>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMyAlbumBinding.inflate(layoutInflater, parent, false)
        val viewHolder = MyAlbumItemViewHolder(binding, itemClickListener)
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        (holder as MyAlbumItemViewHolder).onBind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newData: List<Album>) {
        val diffCallback = AlbumListDiffCallback(items, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items.clear()
        items.addAll(newData);
        diffResult.dispatchUpdatesTo(this);
    }

    inner class MyAlbumItemViewHolder(binding: ItemMyAlbumBinding, itemClickListener: ListItemClickListener) : BaseHolder(binding.root) {

        private val binding: ItemMyAlbumBinding

        init {
            itemView.setOnClickListener {
                itemClickListener.onClick(it, adapterPosition)
            }
            this.binding = binding
        }

        override fun onBind(position: Int) {
            val item = this@MyAlbumsAdapter.items[position]
            val myAlbumItemViewModel = MyAlbumItemViewModel(item)
            binding.viewModel = myAlbumItemViewModel
        }

    }

    internal inner class AlbumListDiffCallback(private val oldList: List<Album>, private val newList: List<Album>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            if (oldItem.id.isNullOrBlank() || newItem.id.isNullOrBlank())
                return oldItem.name.equals(newItem.name)
            return oldItem.id.equals(newItem.id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.equals(newItem)
        }
    }
}