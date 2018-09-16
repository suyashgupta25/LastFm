package de.appsfactory.lastfm.ui.home.topalbums

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.webservice.NetworkState
import de.appsfactory.lastfm.databinding.FragmentTopAlbumsBinding
import de.appsfactory.lastfm.ui.common.base.BaseActivity
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener
import de.appsfactory.lastfm.ui.home.albumdetails.AlbumDetailsFragment
import de.appsfactory.lastfm.utils.ext.addFragment
import javax.inject.Inject

class TopAlbumsFragment : Fragment(), ListItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: TopAlbumsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TopAlbumsViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_top_albums, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
    }

    private fun initBinding(view: View) {
        val binding = DataBindingUtil.bind<FragmentTopAlbumsBinding>(view)
        binding.let {
            it!!.viewModel = viewModel
            it.setLifecycleOwner(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val binding = view?.let { DataBindingUtil.bind<FragmentTopAlbumsBinding>(it) }
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.rvTopAlbums?.layoutManager = linearLayoutManager

        val topAlbumsAdapter = TopAlbumsAdapter(this)
        binding?.rvTopAlbums?.swapAdapter(topAlbumsAdapter, true)
        viewModel.topAlbumsList.observe(this, Observer<PagedList<Album>> { topAlbumsAdapter.submitList(it) })
        viewModel.networkState.observe(this, Observer<NetworkState> { topAlbumsAdapter.setNetworkState(it) })
        executeQuery()
    }

    private fun executeQuery() {
        val artistName = arguments?.getString(getString(R.string.param_fragment_search_bundle))
        viewModel.queryLiveData.value = artistName
    }

    override fun onClick(view: View, position: Int) {
        val album = viewModel.topAlbumsList.value?.get(position)
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.param_fragment_details_bundle),
                album)
        activity?.addFragment((activity as BaseActivity).fragmentContainerId, ::AlbumDetailsFragment, bundle)
    }

    override fun onRetryClick(position: Int) {
        executeQuery()
    }
}