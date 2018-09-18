package de.appsfactory.lastfm.ui.home.searchscreen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.data.model.Artist
import de.appsfactory.lastfm.data.webservice.NetworkState
import de.appsfactory.lastfm.databinding.FragmentSearchBinding
import de.appsfactory.lastfm.ui.common.base.BaseActivity
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener
import de.appsfactory.lastfm.ui.home.topalbums.TopAlbumsFragment
import de.appsfactory.lastfm.utils.ext.addFragment
import de.appsfactory.lastfm.utils.ext.hideKeyboard
import javax.inject.Inject


class SearchFragment : Fragment(), ListItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
    }

    private fun initBinding(view: View) {
        val binding = DataBindingUtil.bind<FragmentSearchBinding>(view)
        binding.let {
            it!!.viewModel = viewModel
            it.setLifecycleOwner(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        addObservers()
    }

    private fun addObservers() {
        val binding = view?.let { DataBindingUtil.bind<FragmentSearchBinding>(it) }
        viewModel.queryLiveData.observe(this, Observer {
            activity?.hideKeyboard(activity, binding?.etSearchArtist)
        })
    }

    private fun initViews() {
        val binding = view?.let { DataBindingUtil.bind<FragmentSearchBinding>(it) }

        val searchAdapter = SearchAdapter(this)
        binding?.rvSearchResults?.swapAdapter(searchAdapter, true)
        viewModel.artistsList.observe(this, Observer<PagedList<Artist>> { searchAdapter.submitList(it) })
        viewModel.networkState.observe(this, Observer<NetworkState> { searchAdapter.setNetworkState(it) })
    }

    override fun onClick(view: View, position: Int) {
        Log.d(TAG, "onClick position=" + position)
        val binding = this.view?.let { DataBindingUtil.bind<FragmentSearchBinding>(it) }
        activity?.hideKeyboard(activity, binding?.etSearchArtist)
        val bundle = Bundle()
        bundle.putString(getString(R.string.param_fragment_search_bundle),
                viewModel.getArtistName(position))
        activity?.addFragment((activity as BaseActivity).fragmentContainerId, ::TopAlbumsFragment, bundle)
    }

    override fun onRetryClick(position: Int) {
        val binding = view?.let { DataBindingUtil.bind<FragmentSearchBinding>(it) }
        viewModel.queryLiveData.value = binding?.etSearchArtist?.text.toString()
    }

    companion object {
        private val TAG = SearchFragment::class.java.getSimpleName()
    }

}