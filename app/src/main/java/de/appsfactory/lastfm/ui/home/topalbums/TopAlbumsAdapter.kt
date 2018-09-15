package de.appsfactory.lastfm.ui.home.topalbums

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.data.NetworkState
import de.appsfactory.lastfm.data.Status
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.model.ImageURL
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener

class TopAlbumsAdapter (val context: Context, val itemClickListener: ListItemClickListener) : PagedListAdapter<Album, RecyclerView.ViewHolder>(Album.DIFF_CALLBACK) {

    private var networkState: NetworkState = NetworkState.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        val viewHolder: RecyclerView.ViewHolder

        when (viewType) {
            R.layout.item_top_album -> {
                view = layoutInflater.inflate(R.layout.item_top_album, parent, false)
                viewHolder = TopAlbumItemViewHolder(view, itemClickListener)
            }
            R.layout.item_network_state -> {
                view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
                viewHolder = NetworkStateItemViewHolder(view, itemClickListener)
            }
            else -> throw IllegalArgumentException("unknown view type")
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_top_album -> (holder as TopAlbumItemViewHolder).bindTo(getItem(position)!!)
            R.layout.item_network_state -> (holder as NetworkStateItemViewHolder).bindView(networkState)
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

    internal class TopAlbumItemViewHolder(itemView: View, itemClickListener: ListItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_album_name)
        private val playcount: TextView = itemView.findViewById(R.id.tv_album_play_count)
        private val icon: ImageView = itemView.findViewById(R.id.imv_top_album)

        init {
            itemView.setOnClickListener {
                itemClickListener.onClick(it, adapterPosition)
            }
        }

        fun bindTo(album: Album) {
            name.text = album.name
            playcount.text = album.playcount.toString()
            val context = icon.context

            val uri = album.getImageURL(ImageURL.Size.MEDIUM).url
            val dimension = context.resources.getDimension(R.dimen.radius_card_corner)
            val transforms = RequestOptions()
                    .transforms(CenterInside(), RoundedCorners(dimension.toInt()))
                    .placeholder(R.mipmap.ic_lastfm_round)
                    .error(R.mipmap.ic_lastfm_round)
            Glide.with(context)
                    .load(uri)
                    .apply(transforms)
                    .into(icon)
        }
    }

    internal class NetworkStateItemViewHolder(itemView: View, listItemClickListener: ListItemClickListener) : RecyclerView.ViewHolder(itemView) {

        private val progressBar: ProgressBar
        private val errorMsg: TextView
        private val button: Button

        init {
            progressBar = itemView.findViewById(R.id.progress_bar)
            errorMsg = itemView.findViewById(R.id.error_msg)
            button = itemView.findViewById(R.id.retry_button)

            button.setOnClickListener { view -> listItemClickListener.onClick(view, adapterPosition) }
        }


        fun bindView(networkState: NetworkState?) {
            if (networkState != null && networkState.status === Status.RUNNING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }

            if (networkState != null && networkState.status === Status.FAILED) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = networkState.msg
                button.visibility = View.VISIBLE
            } else {
                errorMsg.visibility = View.GONE
                button.visibility = View.GONE
            }
        }
    }

}