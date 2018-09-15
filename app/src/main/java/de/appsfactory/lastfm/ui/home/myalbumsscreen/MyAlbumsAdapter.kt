package de.appsfactory.lastfm.ui.home.myalbumsscreen

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.model.ImageURL
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener

class MyAlbumsAdapter(val itemClickListener: ListItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<Album>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        val viewHolder: RecyclerView.ViewHolder

        view = layoutInflater.inflate(R.layout.item_my_album, parent, false)
        viewHolder = MyAlbumItemViewHolder(view, itemClickListener)

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyAlbumItemViewHolder).bindTo(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newData: List<Album>) {
        val diffCallback = AlbumListDiffCallback(items , newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items.clear()
        items.addAll(newData);
        diffResult.dispatchUpdatesTo(this);
    }

    internal class MyAlbumItemViewHolder(itemView: View, itemClickListener: ListItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_my_album_title)
        private val artist: TextView = itemView.findViewById(R.id.tv_my_album_artist)
        private val icon: ImageView = itemView.findViewById(R.id.imv_my_album)

        init {
            itemView.setOnClickListener {
                itemClickListener.onClick(it, adapterPosition)
            }
        }

        fun bindTo(album: Album) {
            name.text = album.name
            artist.text = album.artist?.name
            val context = icon.context

            val uri = album.getImageURL(ImageURL.Size.LARGE).url
            val transforms = RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
            Glide.with(context)
                    .load(uri)
                    .apply(transforms)
                    .into(icon)
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