package de.appsfactory.lastfm.ui.home.searchscreen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.data.NetworkState
import de.appsfactory.lastfm.data.model.Artist
import de.appsfactory.lastfm.databinding.FragmentSearchBinding
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener
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
        lifecycle.addObserver(viewModel)
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
        val binding = view?.let { DataBindingUtil.bind<FragmentSearchBinding>(it) }
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.rvSearchResults?.layoutManager = linearLayoutManager

        val searchAdapter = SearchAdapter(this)
        binding?.rvSearchResults?.swapAdapter(searchAdapter, true)
        viewModel.artistsList.observe(this, Observer<PagedList<Artist>> { searchAdapter.submitList(it) })
        viewModel.networkState.observe(this, Observer<NetworkState> { searchAdapter.setNetworkState(it) })
    }

    override fun onClick(view: View, position: Int) {
        Log.d("TAG", "onClick")
        val direction =
                SearchFragmentDirections.ActionSearchFragmentToTopAlbumFragment(viewModel.getArtistName(position))
        findNavController().navigate(direction)
    }

    override fun onRetryClick(position: Int) {

    }

}

//        val binding = view?.let { DataBindingUtil.bind<FragmentSearchBinding>(it) }
//        RxTextView
//                .textChangeEvents(binding!!.etSearchArtist)
//                .debounce(350, TimeUnit.MILLISECONDS)
//                .distinctUntilChanged()
//                .filter(Predicate {
//                    val searchTerm = it.text().toString()
//                     searchTerm.length > 3 || searchTerm.length == 0
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe{
//                    val currentText = it.text().toString()
//                    if (currentText.length == 0) {
//                        //mainPresenter.loadCachedFoods();
//                    } else {
//                        //cachePreviousFoods();
//                        //mainPresenter.searchFood(currentText);
//                    }
//                }
