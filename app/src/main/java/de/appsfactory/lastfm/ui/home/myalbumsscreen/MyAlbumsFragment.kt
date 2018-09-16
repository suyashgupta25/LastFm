package de.appsfactory.lastfm.ui.home.myalbumsscreen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import dagger.android.support.AndroidSupportInjection
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.databinding.FragmentMyAlbumsBinding
import de.appsfactory.lastfm.ui.common.base.BaseActivity
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener
import de.appsfactory.lastfm.ui.home.albumdetails.AlbumDetailsFragment
import de.appsfactory.lastfm.ui.home.searchscreen.SearchFragment
import de.appsfactory.lastfm.utils.ext.addFragment
import javax.inject.Inject

class MyAlbumsFragment : Fragment(), ListItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var mAdapter: MyAlbumsAdapter? = null

    val viewModel: MyAlbumsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MyAlbumsViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_my_albums, container, false)
        val toolbar = inflate.findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_my_albums)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
    }

    private fun initBinding(view: View) {
        val binding = DataBindingUtil.bind<FragmentMyAlbumsBinding>(view)
        binding.let {
            it!!.viewModel = viewModel
            it.setLifecycleOwner(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addObservers()
    }

    private fun addObservers() {
        val binding = view?.let { DataBindingUtil.bind<FragmentMyAlbumsBinding>(it) }
        viewModel.albums.observe(this, Observer {
            if (mAdapter == null) {
                mAdapter = MyAlbumsAdapter(this)
                binding?.rvMyAlbums?.adapter = mAdapter
            }
            if (it != null) {
                mAdapter!!.setData(it)
                mAdapter!!.notifyDataSetChanged()
                viewModel.hasAlbums.value = !it.isEmpty()
            } else {
                viewModel.hasAlbums.value = false
            }
        })
    }

    override fun onClick(view: View, position: Int) {
        val album = viewModel.albums.value?.get(position)
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.param_fragment_details_bundle),
                album)
        activity?.addFragment((activity as BaseActivity).fragmentContainerId, ::AlbumDetailsFragment, bundle)
    }

    override fun onRetryClick(position: Int) {
        //In case of any error show some message to user
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_my_albums, menu)
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                activity?.addFragment((activity as BaseActivity).fragmentContainerId, ::SearchFragment, Bundle())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}